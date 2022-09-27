package com.mariodicaprio.mamba.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariodicaprio.mamba.entities.Post;
import com.mariodicaprio.mamba.entities.User;
import com.mariodicaprio.mamba.repositories.PostRepository;
import com.mariodicaprio.mamba.repositories.UserRepository;
import com.mariodicaprio.mamba.requests.CreateCommentRequest;
import com.mariodicaprio.mamba.requests.CreatePostRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PostControllerTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    /////////////////////////////////////////////////////////

    @Test
    void byId() throws Exception {
        //create owner of post first
        User owner = new User();
        userRepository.save(owner);

        // create post first
        Post post = new Post();
        post.setOwner(owner);
        postRepository.save(post);

        // assert post can be found
        String url = "/post?postId=" + post.getPostId().toString();
        mockMvc
                .perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getPostId().toString()));
    }

    @Test
    void all() throws Exception {
        // create owner of posts
        User owner = new User();
        userRepository.save(owner);

        // create 20 posts first
        for (int i=0; i<20; i++) {
            Post post = new Post();
            post.setOwner(owner);
            postRepository.save(post);
        }

        // make request
        String url = "/post/all?page=1";
        mockMvc
                .perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value("2"))
                .andExpect(jsonPath("$.index").value("1"))
                .andExpect(jsonPath("$.size").value("15"));
    }

    @Test
    void createPost() throws Exception {
        // create user first
        User user = new User();
        userRepository.save(user);

        // create post request
        CreatePostRequest tmp = new CreatePostRequest(
                "Hello",
                "This is a test case",
                //new CreatePostRequest.CreatePostRequestMedia(media, "image/jpg"),
                //new MockMultipartFile("test image", "test.jpg", "image/jpg", media),
                user.getUserId(),
                new ArrayList<>()
        );
        var request = new MockMultipartFile(
                "request",
                "request.json",
                "application/json",
                objectMapper.writeValueAsBytes(tmp)
        );
        var media = new MockMultipartFile(
                "media",
                "test.jpg",
                "image/jpg",
                new ClassPathResource("img/test.jpg").getInputStream().readAllBytes()
        );

        // make request
        String url = "/post/createPost";
        mockMvc
                .perform(multipart(url).file(request).file(media))
                .andExpect(status().isOk());

        // fetch created post from database
        Post post = postRepository.findByTitle("Hello").get(0);
        // title
        assertThat(post.getTitle()).isEqualTo("Hello");
        // test
        assertThat(post.getText()).isEqualTo("This is a test case");
        // media
        assertThat(post.getMedia().getData()).isEqualTo(media.getBytes());
        // owner
        assertThat(post.getOwner()).isEqualTo(user);
        // reposts
        assertThat(post.getReference()).isEqualTo(null);
        // tags
        assertThat(post.getTags().size()).isEqualTo(0);
        // type
        assertThat(post.getType()).isEqualTo(Post.PostType.POST);
    }

    @Test
    void createComment() throws Exception {
        // create user first
        User user = new User();
        userRepository.save(user);

        // create original post first
        Post original = new Post();
        original.setOwner(user);
        postRepository.save(original);

        // create request
        CreateCommentRequest request = new CreateCommentRequest(
            "This is a comment",
            user.getUserId(),
            original.getPostId()
        );
        String json = objectMapper.writeValueAsString(request);

        // send request
        String url = "/post/createComment";
        mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void createRepost() throws Exception {
        // create user first
        User user = new User();
        userRepository.save(user);

        // create original post first
        Post original = new Post();
        original.setOwner(user);
        postRepository.save(original);

        // create request
        CreateCommentRequest request = new CreateCommentRequest(
                "This is a comment",
                user.getUserId(),
                original.getPostId()
        );
        String json = objectMapper.writeValueAsString(request);

        // send request
        String url = "/post/createRepost";
        mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

}
