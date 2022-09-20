package com.mariodicaprio.mamba.responses;


import com.mariodicaprio.mamba.entities.Post;
import com.mariodicaprio.mamba.entities.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;


@Data
public class UserResponse {

    @NotNull
    private final UUID userId;

    @NotBlank
    private final String username;

    private final String firstName;

    private final String lastName;

    private final byte[] profilePicture;

    @NotNull
    private final List<UUID> posts;

    @NotNull
    private final List<UUID> likes;

    @NotNull
    private final List<UUID> followers;

    @NotNull
    private final List<UUID> following;


    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.profilePicture = user.getProfilePicture();
        this.posts = user.getPosts().stream().map(Post::getPostId).toList();
        this.likes = user.getLikes().stream().map(Post::getPostId).toList();
        this.followers = user.getFollowers().stream().map(User::getUserId).toList();
        this.following = user.getFollowing().stream().map(User::getUserId).toList();
    }

}
