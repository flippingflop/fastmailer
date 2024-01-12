package com.flippingflop.fastmailer.rest.dto.emailTemplate;

import com.flippingflop.fastmailer.util.SequenceUtils;

public class ModifyEmailTemplateRequestTest extends ModifyEmailTemplateRequest {

    public ModifyEmailTemplateRequestTest() {
        this.templateName = SequenceUtils.getString(12);
        this.subject = SequenceUtils.getString(12);
        this.htmlContents = SequenceUtils.getString(12);

        TemplateVariableDto var = new TemplateVariableDto();
        var.keyName = SequenceUtils.getString(12);
        var.defaultValue = SequenceUtils.getString(12);
        this.templateVariableList.add(var);
    }

}