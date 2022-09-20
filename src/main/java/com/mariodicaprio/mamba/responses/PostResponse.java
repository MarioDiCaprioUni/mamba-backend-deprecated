package com.mariodicaprio.mamba.responses;


import com.mariodicaprio.mamba.entities.Post;
import com.mariodicaprio.mamba.entities.User;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Data
public class PostResponse {

    @NotNull
    private final UUID postId;

    private final String title;

    private final String text;

    @NotNull
    private final Date dateCreated;

    @NotNull
    private final Date dateUpdated;

    private final MediaResponse media;

    @NotNull
    private final Post.PostType type;

    private final UUID reposts;

    @NotNull
    private final List<UUID> repostedBy;

    @NotNull
    private final List<UUID> likes;

    @NotNull
    private final UUID owner;


    public PostResponse(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.text = post.getText();
        this.media = (post.getMedia() == null)? null : new MediaResponse(post.getMedia());
        this.type = post.getType();
        this.dateCreated = post.getDateCreated();
        this.dateUpdated = post.getDateUpdated();
        this.reposts = (post.getReference() == null)? null : post.getReference().getPostId();
        this.repostedBy = post.getReferencedBy().stream().map(Post::getPostId).toList();
        this.likes = post.getLikes().stream().map(User::getUserId).toList();
        this.owner = post.getOwner().getUserId();
    }

}
