package com.flippingflop.fastmailer.service;

import com.flippingflop.fastmailer.exception.model.CustomValidationException;
import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import com.flippingflop.fastmailer.repository.EmailRepository;
import com.flippingflop.fastmailer.repository.EmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailValidator {

    final EmailRepository emailRepository;
    final EmailTemplateRepository emailTemplateRepository;

    /**
     * Validator method for sendTemplateEmail.
     * @param recipientEmail (required)
     * @param senderEmail    (optional)
     * @param templateName   (required)
     * @param templateData   (optional)
     * @param uniqueKey      (optional)
     */
    public void sendTemplateEmailValidate(String recipientEmail, String senderEmail, String templateName, Map<String, String> templateData, String uniqueKey) {
        /* Check if required variable provided. */
        if (!StringUtils.hasLength(recipientEmail) || !StringUtils.hasLength(templateName)) {
            throw new CustomValidationException(1, "Required variable not provided.");
        }

        /* Check templateName exists. */
        EmailTemplate emailTemplate = emailTemplateRepository.findByTemplateNameAndIsDeleted(templateName, false);
        if (emailTemplate == null) {
            throw new CustomValidationException(2, "Template does not exist.");
        }

        /* Check if invalid key provided. */
        Set<String> availableKeySet = emailTemplate.getTemplateVariableList().stream()
                .map(var -> var.getKeyName())
                .collect(Collectors.toSet());
        if (!availableKeySet.containsAll(templateData.keySet())) {
            throw new CustomValidationException(3, "Invalid variable key provided.");
        }
    }

}
