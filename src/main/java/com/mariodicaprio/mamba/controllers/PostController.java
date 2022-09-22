package com.mariodicaprio.mamba.controllers;


import com.mariodicaprio.mamba.entities.Post;
import com.mariodicaprio.mamba.requests.CreateCommentRequest;
import com.mariodicaprio.mamba.requests.CreatePostRequest;
import com.mariodicaprio.mamba.requests.CreateRepostRequest;
import com.mariodicaprio.mamba.responses.PageResponse;
import com.mariodicaprio.mamba.responses.PostResponse;
import com.mariodicaprio.mamba.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/post")
@Transactional
public class PostController {

    @Autowired
    private PostService postService;

    ///////////////////////////////////////////////////////////////////////////

    @GetMapping
    @Operation(description = "Fetches a post by its ID")
    @ApiResponse(responseCode = "200", description = "The post with the given ID or null if not found")
    public PostResponse byId(
            @RequestParam(required = false)
            @Parameter(description = "The post's ID")
            UUID postId
    ) {
        Post post = postService.byId(postId);
        if (post == null)
            return null;
        return new PostResponse(post);
    }

    ///////////////////////////////////////////////////////////////////////////

    @GetMapping("/all")
    @Operation(description = "Fetches a page of posts sorted by newest to oldest. The page contains up to 15 posts.")
    @ApiResponse(responseCode = "200", description = "The requested page with up to 15 posts sorted from newest to oldest")
    public PageResponse<PostResponse> all(
            @RequestParam(required = false, defaultValue = "1")
            @Parameter(description = "The page's index (one-based)")
            int page
    ) {
        Page<PostResponse> tmp = postService.all(page).map(PostResponse::new);
        return new PageResponse<>(tmp);
    }

    ///////////////////////////////////////////////////////////////////////////

    @PostMapping("/createPost")
    @Operation(description = "Creates a post")
    public void createPost(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The post's creation request")
            CreatePostRequest request
    ) {
        postService.createPost(request);
    }

    ///////////////////////////////////////////////////////////////////////////

    @PostMapping("/createComment")
    @Operation(description = "Creates a comment")
    public void createComment(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The comment's creation request")
            CreateCommentRequest request
    ) {
        postService.createComment(request);
    }

    ///////////////////////////////////////////////////////////////////////////

    @PostMapping("/createRepost")
    @Operation(description = "Creates a repost")
    public void createRepost(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The repost's creation request")
            CreateRepostRequest request
    ) {
        postService.createRepost(request);
    }

    ///////////////////////////////////////////////////////////////////////////

    @GetMapping("/byUser")
    @Operation(description = "Fetches all posts by a given user.")
    @ApiResponse(responseCode = "200", description = "The requested page with up to 15 posts sorted from newest to oldest")
    public PageResponse<PostResponse> byUser(
            @RequestParam(required = false)
            @Parameter(description = "The username to filter by. If none is given, then no filter is applied.")
            String username,
            @RequestParam(required = false, defaultValue = "1")
            @Parameter(description = "The index of the page (one-based). Default is 1.")
            int page
    ) {
        var tmp = postService.byUsername(username, page).map(PostResponse::new);
        return new PageResponse<>(tmp);
    }

    ///////////////////////////////////////////////////////////////////////////

    @GetMapping("/pictures")
    @Operation(description = "Fetches all posts with a picture")
    @ApiResponse(responseCode = "200", description = "The requested page with up to 15 posts sorted from newest to oldest")
    public PageResponse<PostResponse> pictures(
            @RequestParam(required = false)
            @Parameter(description = "The username to filter by. If none is given, then no filter is applied.")
            String username,
            @RequestParam(required = false, defaultValue = "1")
            @Parameter(description = "The index of the page (one-based). Default is 1.")
            int page
    ) {
        var tmp = postService.picturePostsByUsername(username, page).map(PostResponse::new);
        return new PageResponse<>(tmp);
    }

    ///////////////////////////////////////////////////////////////////////////

    @GetMapping("/videos")
    @Operation(description = "Fetches all posts with a video")
    @ApiResponse(responseCode = "200", description = "The requested page with up to 15 posts sorted from newest to oldest")
    public PageResponse<PostResponse> videos(
            @RequestParam(required = false)
            @Parameter(description = "The username to filter by. If none is given, then no filter is applied.")
            String username,
            @RequestParam(required = false, defaultValue = "1")
            @Parameter(description = "The index of the page (one-based). Default is 1.")
            int page
    ) {
        var tmp = postService.videoPostsByUsername(username, page).map(PostResponse::new);
        return new PageResponse<>(tmp);
    }

    ///////////////////////////////////////////////////////////////////////////

    @GetMapping("/comments")
    @Operation(description = "Fetches all comments by a given user")
    @ApiResponse(responseCode = "200", description = "The requested page with up to 15 posts sorted from newest to oldest")
    public PageResponse<PostResponse> comments(
            @RequestParam
            @Parameter(description = "The username to filter by")
            String username,
            @RequestParam(required = false, defaultValue = "1")
            @Parameter(description = "The index of the page (one-based). Default is 1.")
            int page
    ) {
        var tmp = postService.commentsByUsername(username, page).map(PostResponse::new);
        return new PageResponse<>(tmp);
    }

    ///////////////////////////////////////////////////////////////////////////

    @GetMapping("/reposts")
    @Operation(description = "Fetches all reposts by a given user")
    @ApiResponse(responseCode = "200", description = "The requested page with up to 15 posts sorted from newest to oldest")
    public PageResponse<PostResponse> reposts(
            @RequestParam
            @Parameter(description = "The username to filter by")
            String username,
            @RequestParam(required = false, defaultValue = "1")
            @Parameter(description = "The index of the page (one-based). Default is 1.")
            int page
    ) {
        var tmp = postService.repostsByUsername(username, page).map(PostResponse::new);
        return new PageResponse<>(tmp);
    }

}
