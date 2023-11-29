package com.flippingflop.fastmailer.rest.dto.emailTemplate;

import lombok.Getter;

@Getter
public class SaveEmailTemplateResponse {

    Long id;

    public SaveEmailTemplateResponse(Long id) {
        this.id = id;
    }

}
