package com.mariodicaprio.mamba.requests;


import lombok.Data;

import java.util.UUID;


@Data
public class LikePostRequest {

    private final UUID userId;
    private final UUID postId;
    private final boolean like;

}
