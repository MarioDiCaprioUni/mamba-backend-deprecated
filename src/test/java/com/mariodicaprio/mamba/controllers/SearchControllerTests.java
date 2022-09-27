package com.mariodicaprio.mamba.controllers;


import com.mariodicaprio.mamba.entities.User;
import com.mariodicaprio.mamba.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SearchControllerTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    //////////////////////////////////////////////////////

    @Test
    void users() throws Exception {
        String url = "/search/users";

        // create users first
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setUsername("Hello, World");
        user2.setUsername("Oh Hello there!");
        user3.setUsername("Bye!");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        // make request
        mockMvc
                .perform(get(url).param("expression", "Hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

}
