package com.mariodicaprio.mamba.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariodicaprio.mamba.requests.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RegisterControllerTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    /////////////////////////////////////////////////////////////////////////////////

    @Test
    void register() throws Exception {
        String url = "/register";
        RegisterRequest request = new RegisterRequest("Hello", "helloworld@gmail.com", "World");
        String json = objectMapper.writeValueAsString(request);

        // assert registration works
        mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value("true"));

        // assert registration fails, because user already exists
        mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value("false"));
    }

}
