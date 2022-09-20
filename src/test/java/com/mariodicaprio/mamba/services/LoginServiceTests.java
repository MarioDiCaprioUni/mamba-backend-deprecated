package com.mariodicaprio.mamba.services;


import com.mariodicaprio.mamba.entities.User;
import com.mariodicaprio.mamba.repositories.UserRepository;
import com.mariodicaprio.mamba.requests.LoginRequest;
import com.mariodicaprio.mamba.responses.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class LoginServiceTests {

    @Autowired
    LoginService loginService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    //////////////////////////////////////////////////////////////////////////////////////

    @Test
    void login() {
        LoginRequest request = new LoginRequest("Hello", "World");

        // ensure request fails, because user does not exist
        LoginResponse response = loginService.login(request);
        assertThat(response.isValid()).isFalse();

        // create user
        User user = new User("Hello", "helloworld@gmail.com", passwordEncoder.encode("World"));
        userRepository.save(user);

        System.out.println("Users: " + userRepository.findAll());

        // ensure request now valid
        response = loginService.login(request);
        assertThat(response.isValid()).isTrue();
    }

}
