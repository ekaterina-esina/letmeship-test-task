package com.letmeship.website.parser.exception;

import lombok.Data;

@Data
public class WebsiteParserException extends RuntimeException{
    private final WebsiteParserErrorEnum error;

    private final String details;

    public WebsiteParserException(WebsiteParserErrorEnum error) {
        this.error = error;
        this.details = null;
    }

    public WebsiteParserException(WebsiteParserErrorEnum error, String details) {
        this.error = error;
        this.details = details;
    }
}
