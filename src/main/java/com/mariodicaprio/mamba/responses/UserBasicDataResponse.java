package com.mariodicaprio.mamba.responses;


import com.mariodicaprio.mamba.entities.User;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Data
public class UserBasicDataResponse {

    private final UUID userId;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String description;
    private final byte[] profilePicture;
    private final List<String> followers;
    private final List<String> following;
    private final List<String> friends;

    public UserBasicDataResponse(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.description = user.getDescription();
        this.profilePicture = user.getProfilePicture();
        this.followers = user.getFollowers().stream().map(x -> x.getUserId().toString()).collect(Collectors.toList());
        this.following = user.getFollowing().stream().map(x -> x.getUserId().toString()).collect(Collectors.toList());
        this.friends = user.getFriends().stream().map(x -> x.getUserId().toString()).collect(Collectors.toList());
    }

}
