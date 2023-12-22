package com.flippingflop.fastmailer.rest.dto.emailTemplate;

import lombok.Getter;

@Getter
public class ModifyEmailTemplateResponse {

    String templateName;

    public ModifyEmailTemplateResponse(String templateName) {
        this.templateName = templateName;
    }

}
