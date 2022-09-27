package com.mariodicaprio.mamba.config;


import com.mariodicaprio.mamba.entities.Media;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.UUID;


@Configuration
public class EasyRandomConfig {

    @Bean
    public EasyRandom easyRandom() {
        EasyRandomParameters parameters = new EasyRandomParameters();

        parameters.randomize(UUID.class, () -> null);

        parameters.randomize(Media.class, () -> {
            Media media = new Media();
            media.setType("image/jpg");
            try {
                media.setData(new ClassPathResource("img/test.jpg").getInputStream().readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return media;
        });

        return new EasyRandom(parameters);
    }

}
