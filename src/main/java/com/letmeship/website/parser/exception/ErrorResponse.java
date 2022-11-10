package com.letmeship.website.parser.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(WebsiteParserErrorEnum errorEnum) {
        this.message = errorEnum.getMessage();
    }
}
