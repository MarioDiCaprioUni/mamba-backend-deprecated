package com.mariodicaprio.mamba.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariodicaprio.mamba.requests.LoginRequest;
import com.mariodicaprio.mamba.requests.RegisterRequest;
import com.mariodicaprio.mamba.services.RegisterService;
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
public class LoginControllerTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RegisterService registerService;

    @Autowired
    MockMvc mockMvc;

    /////////////////////////////////////////////////////////////////////////////

    @Test
    void login() throws Exception {
        String url = "/login";
        LoginRequest request = new LoginRequest("Hello", "World");
        String json = objectMapper.writeValueAsString(request);

        // assert login failed, because user does not exist yet
        mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value("false"));

        // now register user
        registerService.register(new RegisterRequest("Hello", "helloworld@gmail.com", "World"));

        // assert login now successful
        mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value("true"));
    }

}
