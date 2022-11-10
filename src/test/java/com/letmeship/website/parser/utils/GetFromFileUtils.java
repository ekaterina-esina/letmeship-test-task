package com.letmeship.website.parser.utils;

import lombok.experimental.UtilityClass;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@UtilityClass
public class GetFromFileUtils {
    public static final String MOCK_SERVER_HTML = "MockServerHtml.txt";
    public static final String MOCK_SERVER_RESPONSE = "MockServerResponse.txt";
    public static final String MOCK_SERVER_RESPONSE_WITHOUT_SPACE = "MockServerResponseWithoutSpace.txt";
    public static final String MOCK_SERVER_HTML_WITHOUT_LINKS = "MockServerHtmlWithoutLinks.txt";

    public String getFromFile(String fileName) throws IOException {
        File file = ResourceUtils.getFile(String.format("classpath:%s", fileName));
        return new String(Files.readAllBytes(file.toPath()));
    }
}
