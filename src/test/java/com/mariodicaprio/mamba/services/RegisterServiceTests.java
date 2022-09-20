package com.mariodicaprio.mamba.services;


import com.mariodicaprio.mamba.requests.RegisterRequest;
import com.mariodicaprio.mamba.responses.RegisterResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class RegisterServiceTests {

    @Autowired
    RegisterService registerService;

    ///////////////////////////////////////////////////////////////////////////////

    @Test
    void register() {
        RegisterRequest request = new RegisterRequest("Hello", "helloworld@gmail.com", "World");

        // ensure request successful
        RegisterResponse response = registerService.register(request);
        assertThat(response.isValid()).isTrue();

        // ensure request fails, because user already exists
        response = registerService.register(request);
        assertThat(response.isValid()).isFalse();
    }

}
