package com.mariodicaprio.mamba.services;


import com.mariodicaprio.mamba.entities.User;
import com.mariodicaprio.mamba.exceptions.DuplicateFriendRequestException;
import com.mariodicaprio.mamba.exceptions.UserDoesNotExistException;
import com.mariodicaprio.mamba.repositories.UserRepository;
import com.mariodicaprio.mamba.requests.FriendRequest;
import com.mariodicaprio.mamba.responses.UserBasicDataResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class UserServiceTests {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    //////////////////////////////////////////////////////////////////////////////////////

    @Test
    void userAll() {
        // create 5 users
        for (int i=0; i<5; i++) {
            User tmp = new User();
            userRepository.save(tmp);
        }

        // assert all users can be fetched
        int numberOfUsers = userService.all(1).getNumberOfElements();
        assertThat(numberOfUsers).isEqualTo(5);
    }

    @Test
    void userById() {
        // create user first
        User user = new User();
        userRepository.save(user);

        // assert user can be found
        User tmp = userRepository.findById(user.getUserId()).orElse(null);
        assertThat(tmp).isNotNull();
    }

    @Test
    void userByUsername() {
        // create user first
        User user = new User();
        user.setUsername("Hello");
        userRepository.save(user);

        // assert user can be found
        User tmp = userService.byUsername("Hello");
        assertThat(tmp).isNotNull();
    }

    @Test
    void userBasicData() {
        // create user first
        User user = new User();
        userRepository.save(user);

        // assert data can be found
        UserBasicDataResponse response = userService.basicData(user.getUserId());
        assertThat(response).isNotNull();
    }

    @Test
    void userSendFriendRequest() throws Exception {
        // create two users first
        User user1 = new User();
        User user2 = new User();
        userRepository.save(user1);
        userRepository.save(user2);

        // create friend request from user1 to user2
        FriendRequest request = new FriendRequest(user1.getUserId(), user2.getUserId());

        // make request and assert it works
        userService.sendFriendRequest(request);

        // make sure request was effective
        assertThat(user1.getSentFriendRequests().contains(user2)).isTrue();
        assertThat(user2.getReceivedFriendRequests().contains(user1)).isTrue();

        // make request again and expect to fail, because the request is duplicate
        Assertions.assertThrows(DuplicateFriendRequestException.class, () -> userService.sendFriendRequest(request));

        // make friend request from user2 to user1 with same expectation
        Assertions.assertThrows(DuplicateFriendRequestException.class, () -> {
            FriendRequest reverse = new FriendRequest(user2.getUserId(), user1.getUserId());
            userService.sendFriendRequest(reverse);
        });

        // make request for nonexistent user
        Assertions.assertThrows(UserDoesNotExistException.class, () -> {
            FriendRequest badRequest = new FriendRequest(user1.getUserId(), UUID.randomUUID());
            userService.sendFriendRequest(badRequest);
        });

        // make sure no request can be sent if user1 and user2 are already friends
        Assertions.assertThrows(DuplicateFriendRequestException.class, () -> {
            userService.acceptFriendRequest(request);
            userService.sendFriendRequest(request);
        });
    }

    @Test
    void acceptFriendRequest() throws Exception {
        // create two users first
        User user1 = new User();
        User user2 = new User();
        userRepository.save(user1);
        userRepository.save(user2);

        // send friend request from user1 to user2
        FriendRequest request = new FriendRequest(user1.getUserId(), user2.getUserId());
        userService.sendFriendRequest(request);

        // accept friend request
        userService.acceptFriendRequest(request);

        // make sure request was effective
        assertThat(user1.getFriends().contains(user2)).isTrue();
        assertThat(user2.getFriends().contains(user1)).isTrue();

        // make sure friend request was deleted
        assertThat(user1.getSentFriendRequests().contains(user2)).isFalse();
        assertThat(user2.getReceivedFriendRequests().contains(user1)).isFalse();
    }

}
