package com.mariodicaprio.mamba.requests;


import lombok.Data;

import java.util.List;
import java.util.UUID;


@Data
public class CreateRepostRequest {

    private final String title;
    private final String text;
    private final UUID ownerId;
    private final UUID referenceId;
    private final List<String> tagNames;

}
