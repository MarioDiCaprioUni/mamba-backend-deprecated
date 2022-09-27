package com.mariodicaprio.mamba.services;


import com.mariodicaprio.mamba.entities.User;
import com.mariodicaprio.mamba.repositories.UserRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class SearchServiceTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SearchService searchService;

    @Autowired
    EasyRandom easyRandom;

    /////////////////////////////////////////////////////////

    @Test
    void searchUsersByUsername() {
        // create users first
        User user1 = easyRandom.nextObject(User.class);
        User user2 = easyRandom.nextObject(User.class);
        User user3 = easyRandom.nextObject(User.class);
        user1.setUsername("Hello, World");
        user2.setUsername("Oh Hello there!");
        user3.setUsername("Bye!");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        // find users
        List<User> users = searchService.searchUsersByUsername("Hello");
        assertThat(users.contains(user1)).isTrue();
        assertThat(users.contains(user2)).isTrue();
        assertThat(users.contains(user3)).isFalse();
    }

}
