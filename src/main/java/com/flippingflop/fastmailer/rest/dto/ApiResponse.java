package com.flippingflop.fastmailer.rest.dto;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    int status;
    String message;
    T data;

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
