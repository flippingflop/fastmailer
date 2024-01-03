package com.flippingflop.fastmailer.rest.dto.emailTemplate;

import lombok.Getter;

@Getter
public class DeleteEmailTemplateResponse {

    boolean deletedFromSes;

    boolean deletedFromDatabase;

    public DeleteEmailTemplateResponse(boolean deletedFromSes, boolean deletedFromDatabase) {
        this.deletedFromSes = deletedFromSes;
        this.deletedFromDatabase = deletedFromDatabase;
    }

}
