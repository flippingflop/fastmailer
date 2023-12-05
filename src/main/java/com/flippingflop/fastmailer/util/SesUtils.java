package com.flippingflop.fastmailer.util;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.flippingflop.fastmailer.exception.model.CustomValidationException;
import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * This class manages all communications for SES settings.
 */
@Log4j2
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

    /**
     * Load email template from SES by its name.<br>
     * Since the result data is generated from SES object, it only contains templateName, subject, htmlContents.
     * @param templateName
     * @return
     * - The template data generated from SES object.<br>
     * - null if template does not exist.
     */
    public EmailTemplate loadEmailTemplate(String templateName) {
        try {
            GetTemplateRequest request = new GetTemplateRequest().withTemplateName(templateName);
            GetTemplateResult result = sesClient.getTemplate(request);
            Template template = result.getTemplate();

            return EmailTemplate.builder()
                    .templateName(template.getTemplateName())
                    .subject(template.getSubjectPart())
                    .htmlContents(template.getHtmlPart())
                    .build();
        } catch (TemplateDoesNotExistException e) {
            log.info(e);
            return null;
        }
    }

}
