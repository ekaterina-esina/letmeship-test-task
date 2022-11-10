package com.letmeship.website.parser.service;

import com.letmeship.website.parser.exception.WebsiteParserErrorEnum;
import com.letmeship.website.parser.exception.WebsiteParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Service
public class WebsiteParserService {
    private static final String USER_AGENT = "Chrome";
    private static final String LINK_CSS_QUERY = "a";
    private static final String LINK_ATTRIBUTE_KEY = "href";

    private static final String STRING_CONTAINS_PROTOCOL_REGEX = "^(http|https)://.*$";

    public Set<String> parse(String url) {
        Set<String> urlSet = new HashSet<>();
        Document doc;
        try {
            doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
        } catch (IOException e) {
            throw new WebsiteParserException(WebsiteParserErrorEnum.ERROR_CONNECT_TO_WEBSITE, e.getMessage());
        }
        Elements links = doc.select(LINK_CSS_QUERY);
        for (Element element : links) {
            String link = element.attr(LINK_ATTRIBUTE_KEY);
            if (!link.matches(STRING_CONTAINS_PROTOCOL_REGEX)) {
                link = createFullLink(link, url);
            }
            urlSet.add(link);
        }

        return urlSet;
    }

    private String createFullLink(String link, String urlString) {
        try {
            URL url = new URL(urlString);
            return UriComponentsBuilder.newInstance()
                    .scheme(url.getProtocol())
                    .host(url.getHost())
                    .path(link)
                    .build()
                    .toUri()
                    .toURL().toString();
        } catch (MalformedURLException e) {
            throw new WebsiteParserException(WebsiteParserErrorEnum.INVALID_REQUEST, e.getMessage());
        }
    }
}
