package com.flippingflop.fastmailer.rest.dto.emailTemplate;

import com.flippingflop.fastmailer.util.SequenceUtils;

import static org.junit.jupiter.api.Assertions.*;

public class ModifyEmailTemplateRequestTest extends ModifyEmailTemplateRequest{

    public ModifyEmailTemplateRequestTest() {
        /* TemplateVariable */
        TemplateVariableDto templateVariableDto = new TemplateVariableDto();
        templateVariableDto.keyName = SequenceUtils.getString(12);
        templateVariableDto.defaultValue = SequenceUtils.getString(12);
        this.templateVariableList.add(templateVariableDto);

        /* EmailTemplate */
        this.templateName = SequenceUtils.getString(12);
        this.subject = SequenceUtils.getString(12);

        // build htmlContents using variable keys.
        StringBuilder sb = new StringBuilder();
        sb.append("<p>");
        this.templateVariableList.forEach(
                var -> {
                    sb.append("{{")
                            .append(var.keyName)
                            .append("}}");
                }
        );
        sb.append("</p>");
        this.htmlContents = sb.toString();
    }

}