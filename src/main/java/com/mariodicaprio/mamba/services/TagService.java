package com.mariodicaprio.mamba.services;


import com.mariodicaprio.mamba.entities.Tag;
import com.mariodicaprio.mamba.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    //////////////////////////////////////////////////////////////////////

    public Tag tagByName(String name) {
        if (name == null)
            return null;
        return tagRepository.findByName(name).orElse(null);
    }

}
