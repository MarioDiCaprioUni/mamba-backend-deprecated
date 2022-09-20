package com.mariodicaprio.mamba.repositories;


import com.mariodicaprio.mamba.entities.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class TagRepositoryTests {
    
    @Autowired
    TagRepository tagRepository;
    
    ///////////////////////////////////////////////////////////////////////

    @Test
    void save() {
        // create tag
        Tag tag = new Tag();
        tagRepository.save(tag);
        // assert tag was saved successfully
        assertThat(tag.getTagId()).isNotNull();
    }

    @Test
    void findById() {
        // create tag first
        Tag tag = new Tag();
        tagRepository.save(tag);
        // assert tag can be found successfully
        Tag tmp = tagRepository.findById(tag.getTagId()).orElse(null);
        assertThat(tmp).isNotNull();
    }

    @Test
    void findByName() {
        // create tag first
        Tag tag = new Tag();
        tag.setName("Hello");
        tagRepository.save(tag);
        // assert tag can be found successfully
        Tag tmp = tagRepository.findByName("Hello").orElse(null);
        assertThat(tmp).isNotNull();
    }

    @Test
    void update() {
        // create tag first
        Tag tag = new Tag();
        tagRepository.save(tag);
        // update tag field
        tag.setName("Hello");
        tagRepository.save(tag);
        // assert name was updated
        Tag tmp = tagRepository.findById(tag.getTagId()).orElse(null);
        String name = (tmp == null)? null : tmp.getName();
        assertThat(name).isEqualTo("Hello");
    }

    @Test
    void delete() {
        // create tag first
        Tag tag = new Tag();
        tagRepository.save(tag);
        // delete tag
        tagRepository.delete(tag);
        // assert tag cannot be found anymore
        Tag tmp = tagRepository.findById(tag.getTagId()).orElse(null);
        assertThat(tmp).isEqualTo(null);
    }
    
}
