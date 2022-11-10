package com.letmeship.website.parser.service;


import com.jayway.jsonpath.JsonPath;
import com.letmeship.website.parser.exception.WebsiteParserErrorEnum;
import com.letmeship.website.parser.exception.WebsiteParserException;
import com.letmeship.website.parser.utils.GetFromFileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.model.MediaType;
import org.mockserver.springtest.MockServerTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest
@MockServerTest
public class WebsiteParserServiceTest {
    private static final String INVALID_WEBSITE_URL = "https://www. google.com";
    private static final String URLSET_FIELD = "urlSet";
    private WebsiteParserService service;
    private MockServerClient mockServerClient;

    private String WEBSITE_URL;

    @BeforeEach
    public void setUp() {
        service = new WebsiteParserService();
        WEBSITE_URL = String.format("http://localhost:%s", mockServerClient.getPort());
    }

    @Test
    public void shouldParseAllUniqueLinksFromWebsiteAndCompleteOnlyLinksWithoutHost() throws Exception {
        String mockedResponse = GetFromFileUtils.getFromFile(GetFromFileUtils.MOCK_SERVER_HTML);
        mockServerClient
                .when(request())
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withContentType(MediaType.TEXT_HTML)
                        .withBody(mockedResponse));
        String json = GetFromFileUtils.getFromFile(GetFromFileUtils.MOCK_SERVER_RESPONSE);
        List<String> expected = parseJson(json);

        Set<String> actual = service.parse(WEBSITE_URL);

        List<String> actualList = new ArrayList<>(actual);
        assertThat(actualList, containsInAnyOrder(expected.toArray()));
    }


    @Test
    public void shouldReturnEmptySetIfNoLinksInWebsite() throws Exception {
        String mockedResponse = GetFromFileUtils.getFromFile(GetFromFileUtils.MOCK_SERVER_HTML_WITHOUT_LINKS);
        mockServerClient
                .when(request())
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withContentType(MediaType.TEXT_HTML)
                        .withBody(mockedResponse));
        Set<String> expected = new HashSet<>();

        Set<String> actual = service.parse(WEBSITE_URL);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldThrowExceptionWhenImpossibleConnectToWebsite() {
        mockServerClient
                .when(request())
                .respond(response()
                        .withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR_500.code()));

        assertThrows(
                WebsiteParserException.class,
                () -> service.parse(WEBSITE_URL),
                WebsiteParserErrorEnum.ERROR_CONNECT_TO_WEBSITE.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUrlInvalid() {
        assertThrows(
                WebsiteParserException.class,
                () -> service.parse(INVALID_WEBSITE_URL),
                WebsiteParserErrorEnum.INVALID_REQUEST.getMessage());
    }

    private List<String> parseJson(String json) {
        return JsonPath.parse(json).read(URLSET_FIELD);
    }
}
