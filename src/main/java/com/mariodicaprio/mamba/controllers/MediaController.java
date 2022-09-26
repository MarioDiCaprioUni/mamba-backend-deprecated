package com.mariodicaprio.mamba.controllers;


import com.mariodicaprio.mamba.entities.Media;
import com.mariodicaprio.mamba.services.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    /////////////////////////////////////////////////////////////////

    @GetMapping
    @Operation(description = "Fetches a media by its ID")
    @ApiResponse(responseCode = "200", description = "The requested media or null if none was found")
    @Transactional
    public Resource get(
            @RequestParam
            @Parameter(description = "The media's ID")
            UUID id
    ) {
        Media media = mediaService.getById(id);
        if (media == null) {
            return null;
        }
        return new ByteArrayResource(media.getData());
    }

    /////////////////////////////////////////////////////////////////

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(description = "Uploads a media")
    public void post(
            @RequestPart
            @Parameter(description = "The media to upload")
            MultipartFile media
    ) throws IOException {
        mediaService.save(media);
    }

}
