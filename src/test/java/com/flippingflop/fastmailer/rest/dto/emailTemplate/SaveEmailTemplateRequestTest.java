package com.flippingflop.fastmailer.rest.dto.emailTemplate;

import com.flippingflop.fastmailer.util.SequenceUtils;

public class SaveEmailTemplateRequestTest extends SaveEmailTemplateRequest {

    public SaveEmailTemplateRequestTest() {
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

    public void templateNameDuplicated(String templateName) {
        this.templateName = templateName;
    }

    public void requiredVariablesNotProvided(String eraseTarget) {
        TemplateVariableDto templateVariableDto = this.templateVariableList.get(0);

        switch (eraseTarget) {
            case "key" -> templateVariableDto.keyName = "";
            case "value" -> templateVariableDto.defaultValue = "";
            default -> throw new RuntimeException("invalid parameter provided");
        }
    }

    public void keyNotConsumed() {
        this.htmlContents = SequenceUtils.getString(12);
    }

}