package com.flippingflop.fastmailer.service;

import com.flippingflop.fastmailer.model.vo.Email;
import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import com.flippingflop.fastmailer.repository.EmailRepository;
import com.flippingflop.fastmailer.repository.EmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

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

}
