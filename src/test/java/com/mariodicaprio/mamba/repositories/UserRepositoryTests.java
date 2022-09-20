package com.mariodicaprio.mamba.repositories;


import com.mariodicaprio.mamba.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    //////////////////////////////////////////////////////////////////////////////

    @Test
    void save() {
        // create user
        User user = new User();
        userRepository.save(user);
        // assert user was saved successfully
        assertThat(user.getUserId()).isNotNull();
    }

    @Test
    void findById() {
        // create user first
        User user = new User();
        userRepository.save(user);
        // assert user can be found successfully
        User tmp = userRepository.findById(user.getUserId()).orElse(null);
        assertThat(tmp).isNotNull();
    }

    @Test
    void findByUsername() {
        // create user first
        User user = new User();
        user.setUsername("Hello");
        userRepository.save(user);
        // assert user can be found successfully
        User tmp = userRepository.findByUsername("Hello").orElse(null);
        assertThat(tmp).isNotNull();
    }

    @Test
    void findByEmail() {
        // create user first
        User user = new User();
        user.setEmail("helloworld@gmail.com");
        userRepository.save(user);
        // assert user can be found successfully
        int tmp = userRepository.findByEmail("helloworld@gmail.com").size();
        assertThat(tmp).isGreaterThan(0);
    }

    @Test
    void existsByUsername() {
        // create user first
        User user = new User();
        user.setUsername("Hello");
        userRepository.save(user);
        // assert user exists
        assertThat(userRepository.existsByUsername("Hello")).isTrue();
    }

    @Test
    void update() {
        // create user first
        User user = new User();
        userRepository.save(user);
        // update user field
        user.setUsername("Hello");
        userRepository.save(user);
        // assert username was updated
        User tmp = userRepository.findById(user.getUserId()).orElse(null);
        String username = (tmp == null)? null : tmp.getUsername();
        assertThat(username).isEqualTo("Hello");
    }

    @Test
    void delete() {
        // create user first
        User user = new User();
        userRepository.save(user);
        // delete user
        userRepository.delete(user);
        // assert user cannot be found anymore
        User tmp = userRepository.findById(user.getUserId()).orElse(null);
        assertThat(tmp).isEqualTo(null);
    }

    @Test
    void findByUsernameContaining() {
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

        // find users
        List<User> users = userRepository.findByUsernameContaining("Hello");
        assertThat(users.contains(user1)).isTrue();
        assertThat(users.contains(user2)).isTrue();
        assertThat(users.contains(user3)).isFalse();
    }

}
