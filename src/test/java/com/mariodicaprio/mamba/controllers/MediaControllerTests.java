package com.mariodicaprio.mamba.controllers;


import com.mariodicaprio.mamba.entities.Media;
import com.mariodicaprio.mamba.repositories.MediaRepository;
import com.mariodicaprio.mamba.services.MediaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MediaControllerTests {

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    MediaService mediaService;

    @Autowired
    MockMvc mockMvc;

    ///////////////////////////////////////////////////////////

    @Test
    void post() throws Exception {
        // create multipart file
        var media = new MockMultipartFile(
                "media",
                "test.jpg",
                "image/jpg",
                new ClassPathResource("img/test.jpg").getInputStream().readAllBytes()
        );

        // send request
        String url = "/media";
        mockMvc
                .perform(multipart(url).file(media))
                .andExpect(status().isOk());

        // fetch data and expect media to exist
        assertThat(mediaRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void get() throws Exception {
        // create media first
        Media media = new Media();
        media.setType("image/jpg");
        media.setData(new ClassPathResource("img/test.jpg").getInputStream().readAllBytes());
        mediaRepository.save(media);

        // make request
        String url = "/media";
        mockMvc
                .perform(MockMvcRequestBuilders.get(url).param("id", media.getMediaId().toString()))
                .andExpect(status().isOk());
    }

}
