package com.mariodicaprio.mamba.services;


import com.google.common.collect.Ordering;
import com.mariodicaprio.mamba.entities.Post;
import com.mariodicaprio.mamba.repositories.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class PostServiceTests {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    ///////////////////////////////////////////////////////////////////////

    @Test
    void postAll() throws InterruptedException {
        // create 20 posts
        for (int i=0; i<20; i++) {
            Post post = new Post();
            post.setTitle("Post #" + i);
            postRepository.save(post);
            // sleep to sort posts by date created
            Thread.sleep(10);
        }

        // fetch first 15 posts (page 1)
        Page<Post> posts = postService.all(1);

        // assert size = 15
        assertThat(posts.getSize()).isEqualTo(15);

        // assert posts are sorted by date created (descending)
        Comparator<Post> comparator = Comparator.comparing(Post::getDateCreated).reversed();
        boolean isOrdered = Ordering.from(comparator).isOrdered(posts);
        assertThat(isOrdered).isTrue();
    }

}
