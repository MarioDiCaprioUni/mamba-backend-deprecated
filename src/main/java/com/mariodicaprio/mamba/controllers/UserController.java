package com.mariodicaprio.mamba.controllers;


import com.mariodicaprio.mamba.entities.User;
import com.mariodicaprio.mamba.requests.FriendRequest;
import com.mariodicaprio.mamba.requests.LikePostRequest;
import com.mariodicaprio.mamba.responses.PageResponse;
import com.mariodicaprio.mamba.responses.UserBasicDataResponse;
import com.mariodicaprio.mamba.responses.UserResponse;
import com.mariodicaprio.mamba.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


/**
 * This controller handles the route {@code /user}. This class
 * implements delegate methods from {@link UserService}.
 */
@RestController
@RequestMapping("/user")
@Transactional
public class UserController {

    @Autowired
    private UserService userService;

    /////////////////////////////////////////////////////////////////////////

    @GetMapping
    @Operation(description = "Finds a user by their ID")
    @ApiResponse(responseCode = "200", description = "The user with the given ID or null if not found")
    public UserResponse byId(
            @RequestParam(required = false)
            @Parameter(description = "The user's ID")
            UUID userId
    ) {
        User user = userService.byId(userId);
        if (user == null)
            return null;
        return new UserResponse(user);
    }

    /////////////////////////////////////////////////////////////////////////

    @GetMapping("/byUsername")
    @Operation(description = "Finds a user by their username")
    @ApiResponse(responseCode = "200", description = "The user with the given username or null if not found")
    public UserResponse byUsername(
            @RequestParam(required = false)
            @Parameter(description = "The user's username")
            String username
    ) {
        User user = userService.byUsername(username);
        if (user == null)
            return null;
        return new UserResponse(user);
    }

    /////////////////////////////////////////////////////////////////////////

    @GetMapping("/all")
    @Operation(description = "Fetches a page containing all users at the page's index. The page contains up to 15 users and is unsorted.")
    @ApiResponse(responseCode = "200", description = "The page with the given index containing up to 15 users and is unsorted")
    public PageResponse<UserResponse> all(
            @RequestParam
            @Parameter(description = "The index of the page to load (one-based)")
            int page
    ) {
        Page<UserResponse> tmp = userService.all(page).map(UserResponse::new);
        return new PageResponse<>(tmp);
    }

    /////////////////////////////////////////////////////////////////////////

    @PostMapping("/like")
    @Operation(description = "A request that is sent when a user likes/unlikes a post")
    public void likePost(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The like request")
            LikePostRequest request
    ) {
        userService.likePost(request);
    }

    /////////////////////////////////////////////////////////////////////////

    @GetMapping("/basicData")
    @Operation(description = "Retrieves a user's basic data given their ID")
    public UserBasicDataResponse basicData(
            @RequestParam
            @Parameter(description = "The user's ID")
            UUID userId
    ) {
        return userService.basicData(userId);
    }

    /////////////////////////////////////////////////////////////////////////

    @PostMapping("/sendFriendRequest")
    @Operation(description = "Sends a friend request from one user to another")
    public void sendFriendRequest(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The friend request to send")
            FriendRequest request
    ) throws Exception {
        userService.sendFriendRequest(request);
    }

    /////////////////////////////////////////////////////////////////////////

    @PostMapping("/acceptFriendRequest")
    @Operation(description = "Accepts a pending friend request")
    public void acceptFriendRequest(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The friend request to accept")
            FriendRequest request
    ) throws Exception {
        userService.acceptFriendRequest(request);
    }

}
