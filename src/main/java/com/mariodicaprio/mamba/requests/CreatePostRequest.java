package com.mariodicaprio.mamba.requests;


import lombok.Data;

import java.util.List;
import java.util.UUID;


@Data
public class CreatePostRequest {

    private final String title;
    private final String text;
    private final UUID ownerId;
    private final List<String> tagNames;

}
