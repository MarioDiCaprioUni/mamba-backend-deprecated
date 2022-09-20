package com.mariodicaprio.mamba.responses;


import com.mariodicaprio.mamba.entities.Media;
import lombok.Data;

import java.util.UUID;


@Data
public class MediaResponse {

    private final UUID mediaId;
    private final byte[] data;
    private final String type;

    public MediaResponse(Media media) {
        this.mediaId = media.getMediaId();
        this.data = media.getData();
        this.type = media.getType();
    }

}
