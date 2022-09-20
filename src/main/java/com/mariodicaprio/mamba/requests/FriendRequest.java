package com.mariodicaprio.mamba.requests;


import lombok.Data;

import java.util.UUID;


@Data
public class FriendRequest {

    private final UUID from;
    private final UUID to;

}
