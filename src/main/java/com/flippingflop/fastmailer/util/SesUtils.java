package com.flippingflop.fastmailer.util;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.flippingflop.fastmailer.exception.model.CustomValidationException;
import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * This class manages all communications for SES settings.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class SesUtils {

    @Value("${fastmailer.source-name}")
    String sourceName;

    final Gson gson;
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

    /**
     * Send email using template.
     * @param templateName
     * @param senderEmail
     * @param recipientEmail
     * @param variables
     * @throws RuntimeException
     *      indicates that sending email has failed.
     */
    public void sendTemplateEmail(String templateName, String senderEmail, String recipientEmail, Map<String, String> variables) {
        try {
            Destination destination = new Destination().withToAddresses(recipientEmail);
            SendTemplatedEmailRequest request = new SendTemplatedEmailRequest()
                    .withTemplate(templateName)
                    .withSource(encodeSourceName(sourceName) + '<' + senderEmail + '>')
                    .withDestination(destination)
                    .withTemplateData(gson.toJson(variables));
            sesClient.sendTemplatedEmail(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate source name of email. It encodes given string using RFC 2047 MIME so that source can be consumed by SES.
     * @param name
     * @return
     * - The encoded name String<br>
     * - null if encoding fails or empty name provided
     */
    private String encodeSourceName(String name) {
        try {
            return StringUtils.hasLength(name) ? MimeUtility.encodeText(name, "utf-8", "Q") : null;
        } catch (UnsupportedEncodingException e) {
            log.error(e);
        }

        return null;
    }

}
