package com.flippingflop.fastmailer.service;

import com.flippingflop.fastmailer.model.enums.email.EmailStatus;
import com.flippingflop.fastmailer.model.vo.Email;
import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import com.flippingflop.fastmailer.repository.EmailRepository;
import com.flippingflop.fastmailer.repository.EmailTemplateRepository;
import com.flippingflop.fastmailer.util.SesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmailService {

    final SesUtils sesUtils;
    final EmailValidator emailValidator;
    final EmailRepository emailRepository;
    final EmailTemplateRepository emailTemplateRepository;

    /**
     * It does not send email immediately but save email as a pending state.
     * Email sending will be triggered by a separate scheduler.
     * @param recipientEmail    (required)
     * @param senderEmail       (optional)
     * @param templateName      (required)
     * @param templateData      (optional)
     * @param uniqueKey         (optional)
     */
    @Transactional
    public void queueTemplateEmail(String recipientEmail, String senderEmail, String templateName, Map<String, String> templateData, String uniqueKey) {
        /* Validate */
        emailValidator.queueTemplateEmailValidate(recipientEmail, senderEmail, templateName, templateData, uniqueKey);

        /* Save email as a pending state */
        // Load template
        EmailTemplate template = emailTemplateRepository.findByTemplateNameAndIsDeleted(templateName, false);

        // Setup template variables
        Map<String, String> usingDataMap = new HashMap<>();
        template.getTemplateVariableList().forEach(
                templateVariable -> {
                    String providedValue = templateData.get(templateVariable.getKeyName());

                    // If template value is not provided, use default value.
                    String value = providedValue == null ? templateVariable.getDefaultValue() : providedValue;
                    usingDataMap.put(templateVariable.getKeyName(), value);
                }
        );

        // Save email
        Email email = Email.builder()
                .emailTemplate(template)
                .recipientEmail(recipientEmail)
                .senderEmail(senderEmail)
                .templateData(usingDataMap)
                .uniqueKey(uniqueKey)
                .build();
        emailRepository.save(email);
    }

    /**
     * Send an email using SES template.
     * The email must be persisted before being passed as a parameter.
     * This method works on a separate transaction to persist its sending attempt result,
     * regardless of the status of any existing transaction.
     * @param email
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendTemplateEmail(Email email) {
        /* Validate */
        emailValidator.sendTemplateEmailValidate(email);

        /* Update attempt result */
        emailRepository.save(email);

        /* Send the email using SES template. */
        try {
            sesUtils.sendTemplateEmail(
                    email.getEmailTemplate().getTemplateName(),
                    email.getSenderEmail(),
                    email.getRecipientEmail(),
                    email.getTemplateData()
            );

            email.markAsSentSuccessfully();
        } catch (RuntimeException e) {
            log.error(e);
            email.markAsFailed();
        }
    }

    /**
     * Load all pending emails from database and send them with a loop.
     * Its status gets updated regarding the result of SES request.
     * Each email works with separated transaction.
     */
    @Transactional
    public void sendAllPendingEmails() {
        List<Email> pendingList = emailRepository.findAllByStatusAndIsDeleted(EmailStatus.PENDING, false);
        pendingList.forEach(this::sendTemplateEmail);
    }

}
