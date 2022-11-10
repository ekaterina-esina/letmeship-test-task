package com.letmeship.website.parser.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Set;

@Data
@AllArgsConstructor
public class WebsiteParserResponse {
    private Set<String> urlSet;
}
