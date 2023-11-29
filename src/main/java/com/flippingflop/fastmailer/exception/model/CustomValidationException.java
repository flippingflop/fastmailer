package com.flippingflop.fastmailer.exception.model;

import lombok.Getter;

public class CustomValidationException extends RuntimeException {

    @Getter
    int code;

    public CustomValidationException(String message) {
        super(message);
    }

    public CustomValidationException(int code, String message) {
        super(message);

        this.code = code;
    }

}
