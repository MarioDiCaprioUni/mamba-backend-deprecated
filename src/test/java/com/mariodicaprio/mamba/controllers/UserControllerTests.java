package com.mariodicaprio.mamba.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariodicaprio.mamba.entities.Post;
import com.mariodicaprio.mamba.entities.User;
import com.mariodicaprio.mamba.repositories.PostRepository;
import com.mariodicaprio.mamba.repositories.UserRepository;
import com.mariodicaprio.mamba.requests.FriendRequest;
import com.mariodicaprio.mamba.requests.LikePostRequest;
import com.mariodicaprio.mamba.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    //////////////////////////////////////////////////////////////////////////////////////

    @Test
    void byId() throws Exception {
        // create user first
        User user = new User();
        userRepository.save(user);

        // make request
        String url = "/user?userId=" + user.getUserId();
        mockMvc.
                perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user.getUserId().toString()));
    }

    @Test
    void byUsername() throws Exception {
        // create user first
        User user = new User();
        user.setUsername("Hello");
        userRepository.save(user);

        // make request
        String url = "/user/byUsername?username=" + user.getUsername();
        mockMvc.
                perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void all() throws Exception {
        String url = "/user/all?page=1";

        // create 20 users
        for (int i=0; i<20; i++) {
            User user = new User();
            userRepository.save(user);
        }

        // make request
        mockMvc
                .perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value("2"))
                .andExpect(jsonPath("$.index").value("1"))
                .andExpect(jsonPath("$.size").value("15"));
    }

    @Test
    void like() throws Exception {
        String url = "/user/like";

        // create user first
        User user = new User();
        userRepository.save(user);

        // create post first
        Post post = new Post();
        postRepository.save(post);

        // create like request
        LikePostRequest request = new LikePostRequest(user.getUserId(), post.getPostId(), true);
        String json = objectMapper.writeValueAsString(request);

        // send like request
        mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        // ensure like was successful
        user = userRepository.findById(user.getUserId()).orElseThrow();
        post = postRepository.findById(post.getPostId()).orElseThrow();
        assertThat(user.getLikes().contains(post)).isTrue();
        assertThat(post.getLikes().contains(user)).isTrue();

        // create unlike request
        request = new LikePostRequest(user.getUserId(), post.getPostId(), false);
        json = objectMapper.writeValueAsString(request);

        // send unlike request
        mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        // ensure like was successful
        user = userRepository.findById(user.getUserId()).orElseThrow();
        post = postRepository.findById(post.getPostId()).orElseThrow();
        assertThat(user.getLikes().contains(post)).isFalse();
        assertThat(post.getLikes().contains(user)).isFalse();
    }

    @Test
    void basicData() throws Exception {
        String url = "/user/basicData";

        // create user first
        User user = new User();
        userRepository.save(user);

        // send request
        mockMvc
                .perform(get(url).param("userId", user.getUserId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user.getUserId().toString()));
    }

    @Test
    void sendFriendRequest() throws Exception {
        String url = "/user/sendFriendRequest";

        // create two users first
        User user1 = new User();
        User user2 = new User();
        userRepository.save(user1);
        userRepository.save(user2);

        // create friend request from user1 to user2
        FriendRequest request = new FriendRequest(user1.getUserId(), user2.getUserId());
        String json = objectMapper.writeValueAsString(request);

        // send request and assert status is ok
        mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        // send request again and expect to fail, because request is duplicate
        mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isLoopDetected());
    }

    @Test
    void acceptFriendRequest() throws Exception {
        String url = "/user/acceptFriendRequest";

        // create two users first
        User user1 = new User();
        User user2 = new User();
        userRepository.save(user1);
        userRepository.save(user2);

        // create friend request from user1 to user2
        FriendRequest request = new FriendRequest(user1.getUserId(), user2.getUserId());
        String json = objectMapper.writeValueAsString(request);

        // fulfill friend request (via service)
        userService.sendFriendRequest(request);

        // send friend request
        mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

}
