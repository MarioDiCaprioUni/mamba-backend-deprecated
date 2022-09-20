package com.mariodicaprio.mamba.requests;


import lombok.Data;

import java.util.UUID;


@Data
public class CreateCommentRequest {

    private final String text;
    private final UUID ownerId;
    private final UUID referenceId;

}
