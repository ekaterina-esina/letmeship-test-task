package com.letmeship.website.parser.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class WebsiteParserErrorHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        e.getBindingResult().getAllErrors().stream().map(error -> {
                            if (error instanceof FieldError) {
                                return String.format("Field=%s, Message=%s", ((FieldError) error).getField(),
                                        error.getDefaultMessage());
                            }
                            return error.getDefaultMessage();
                        }).collect(Collectors.joining("; "))),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleExceptions(Exception e) {
        if (e instanceof WebsiteParserException) {
            log.error(String.format("WebsiteParserException, Message=%s, Details=%s",
                    ((WebsiteParserException) e).getError().getMessage(),
                    ((WebsiteParserException) e).getDetails()));
            return new ResponseEntity<>(
                    new ErrorResponse(((WebsiteParserException) e).getError()),
                    HttpStatus.resolve(((WebsiteParserException) e).getError().getCode()));
        }

        log.error(String.format("Exception=%s, Message=%s", e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(
                new ErrorResponse(WebsiteParserErrorEnum.UNEXPECTED_ERROR.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
