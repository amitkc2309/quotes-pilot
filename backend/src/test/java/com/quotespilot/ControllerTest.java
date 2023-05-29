package com.quotespilot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quotespilot.dto.QuoteDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerTest {
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public  void testPublicAPI() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/quote/random")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        //result.getAsyncResult(2000) is being used because we are calling a Mono method (Async)
        String jsonResponse = objectMapper.writeValueAsString(result.getAsyncResult(2000));
        System.out.println("****************************"+jsonResponse);
        Assert.assertTrue("Response should contain the word 'text'", jsonResponse.contains("text"));
    }

}
