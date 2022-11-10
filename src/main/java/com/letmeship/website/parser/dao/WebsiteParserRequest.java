package com.letmeship.website.parser.dao;

import com.letmeship.website.parser.validation.Url;
import lombok.Data;

@Data
public class WebsiteParserRequest {
    @Url
    private String url;
}
