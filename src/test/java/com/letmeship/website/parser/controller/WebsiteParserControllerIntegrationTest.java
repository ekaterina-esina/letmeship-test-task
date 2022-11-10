package com.letmeship.website.parser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letmeship.website.parser.WebsiteParserApplication;
import com.letmeship.website.parser.dao.WebsiteParserRequest;
import com.letmeship.website.parser.utils.GetFromFileUtils;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.model.MediaType;
import org.mockserver.springtest.MockServerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = WebsiteParserApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@MockServerTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WebsiteParserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MockServerClient mockServerClient;

    @Test
    public void shouldParseWebsite() throws Exception {
        String mockedResponse = GetFromFileUtils.getFromFile(GetFromFileUtils.MOCK_SERVER_HTML);
        mockServerClient
                .when(request())
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withContentType(MediaType.TEXT_HTML)
                        .withBody(mockedResponse));
        String expected = GetFromFileUtils.getFromFile(GetFromFileUtils.MOCK_SERVER_RESPONSE_WITHOUT_SPACE);
        WebsiteParserRequest request = new WebsiteParserRequest();
        request.setUrl(String.format("http://localhost:%s", mockServerClient.getPort()));
        String json = objectMapper.writeValueAsString(request);

        MvcResult response = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(WebsiteParserController.ENDPOINT_NAME)
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        String actual = response.getResponse().getContentAsString();
        System.out.println(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldFailIfUrlDoesNotContainTopLevelDomain() throws Exception {
        String expected = "{\"message\":\"Field=url, Message=Invalid URL\"}";
        WebsiteParserRequest request = new WebsiteParserRequest();
        request.setUrl("https://www.google");
        String json = objectMapper.writeValueAsString(request);

        MvcResult response = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(WebsiteParserController.ENDPOINT_NAME)
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actual = response.getResponse().getContentAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldFailIfUrlDoesNotContainMiddleDomain() throws Exception {
        String expected = "{\"message\":\"Field=url, Message=Invalid URL\"}";
        WebsiteParserRequest request = new WebsiteParserRequest();
        request.setUrl("https://www..com");
        String json = objectMapper.writeValueAsString(request);

        MvcResult response = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(WebsiteParserController.ENDPOINT_NAME)
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actual = response.getResponse().getContentAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldFailIfUrlDoesNotContainProtocol() throws Exception {
        String expected = "{\"message\":\"Field=url, Message=Invalid URL\"}";
        WebsiteParserRequest request = new WebsiteParserRequest();
        request.setUrl("www.google.com");
        String json = objectMapper.writeValueAsString(request);

        MvcResult response = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(WebsiteParserController.ENDPOINT_NAME)
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actual = response.getResponse().getContentAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldFailIfUrlContainsSpace() throws Exception {
        String expected = "{\"message\":\"Field=url, Message=Invalid URL\"}";
        WebsiteParserRequest request = new WebsiteParserRequest();
        request.setUrl("https://www. google.com");
        String json = objectMapper.writeValueAsString(request);

        MvcResult response = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(WebsiteParserController.ENDPOINT_NAME)
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actual = response.getResponse().getContentAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldFailIfUrlIsEmpty() throws Exception {
        String expected = "{\"message\":\"Field=url, Message=Invalid URL\"}";
        WebsiteParserRequest request = new WebsiteParserRequest();
        request.setUrl("");
        String json = objectMapper.writeValueAsString(request);

        MvcResult response = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(WebsiteParserController.ENDPOINT_NAME)
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actual = response.getResponse().getContentAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldFailIfImpossibleConnectToUrl() throws Exception {
        String expected = "{\"message\":\"Field=url, Message=Invalid URL\"}";
        WebsiteParserRequest request = new WebsiteParserRequest();
        request.setUrl(String.format("http://localhost:%s", 80));
        String json = objectMapper.writeValueAsString(request);

        MvcResult response = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(WebsiteParserController.ENDPOINT_NAME)
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actual = response.getResponse().getContentAsString();
        assertEquals(expected, actual);
    }
}
