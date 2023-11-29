package com.flippingflop.fastmailer.util;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.flippingflop.fastmailer.exception.model.CustomValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * This class manages all communications for SES settings.
 */
@Component
@RequiredArgsConstructor
public class SesUtils {

    final AmazonSimpleEmailService sesClient;

    /**
     * Upload email template to SES
     * @param templateName
     * @param subjectPart
     * @param htmlPart
     * @param textPart
     */
    public void saveEmailTemplate(String templateName, String subjectPart, String htmlPart, String textPart) {
        Template template = new Template();
        template.setTemplateName(templateName);
        template.setSubjectPart(subjectPart);
        template.setHtmlPart(htmlPart);
        template.setTextPart(textPart);

        try {
            CreateTemplateRequest request = new CreateTemplateRequest().withTemplate(template);
            sesClient.createTemplate(request);
        } catch (AlreadyExistsException | InvalidTemplateException | LimitExceededException e) {
            throw new CustomValidationException(e.getMessage());
        }
    }

}
