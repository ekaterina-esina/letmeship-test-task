package com.letmeship.website.parser.exception;

import lombok.Getter;

@Getter
public enum WebsiteParserErrorEnum {
    INVALID_REQUEST(400, "Invalid url"),
    ERROR_CONNECT_TO_WEBSITE(500, "Error connect to website"),

    UNEXPECTED_ERROR(999, "Unexpected error");
    private final int code;

    private final String message;

    WebsiteParserErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
