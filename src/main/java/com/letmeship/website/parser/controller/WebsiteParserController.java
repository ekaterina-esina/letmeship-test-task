package com.letmeship.website.parser.controller;

import com.letmeship.website.parser.dao.WebsiteParserRequest;
import com.letmeship.website.parser.dao.WebsiteParserResponse;
import com.letmeship.website.parser.service.WebsiteParserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = WebsiteParserController.ENDPOINT_NAME)
public class WebsiteParserController {
    public static final String ENDPOINT_NAME = "/api/v1/parser";

    private final WebsiteParserService service;


    public WebsiteParserController(WebsiteParserService service) {
        this.service = service;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebsiteParserResponse parse(@RequestBody @Valid WebsiteParserRequest request) {
        return new WebsiteParserResponse(service.parse(request.getUrl()));
    }
}
