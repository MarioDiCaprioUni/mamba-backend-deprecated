package com.mariodicaprio.mamba.services;


import com.mariodicaprio.mamba.entities.Media;
import com.mariodicaprio.mamba.repositories.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    ////////////////////////////////////////////////////////////////////////

    public void save(MultipartFile file) throws IOException {
        Media media = new Media();
        media.setType(file.getContentType());
        media.setData(file.getBytes());
        mediaRepository.save(media);
    }

    public Media getById(UUID id) {
        if (id == null) {
            return null;
        }
        return mediaRepository.getById(id);
    }

}
