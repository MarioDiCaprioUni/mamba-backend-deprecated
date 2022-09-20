package com.mariodicaprio.mamba.repositories;


import com.mariodicaprio.mamba.entities.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class PostRepositoryTests {

    @Autowired
    PostRepository postRepository;

    /////////////////////////////////////////////////////////////////////////////////////

    @Test
    void save() {
        // create post
        Post post = new Post();
        postRepository.save(post);
        // assert post was saved
        assertThat(post.getPostId()).isNotNull();
    }

    @Test
    void findById() {
        // create post
        Post post = new Post();
        postRepository.save(post);
        // assert post can be found
        Post tmp = postRepository.findById(post.getPostId()).orElse(null);
        assertThat(tmp).isNotNull();
    }

    @Test
    void findByTitle() {
        // create post
        Post post = new Post();
        post.setTitle("Hello, World!");
        postRepository.save(post);
        // assert post can be found by title
        List<Post> posts = postRepository.findByTitle("Hello, World!");
        assertThat(posts.size()).isEqualTo(1);
    }

    @Test
    void update() {
        // create post
        Post post = new Post();
        postRepository.save(post);
        // update post title
        post.setTitle("Hello, World!");
        postRepository.save(post);
        // assert title was updated
        Post tmp = postRepository.findById(post.getPostId()).orElse(null);
        assertThat(tmp).isNotNull();
        assertThat(tmp.getTitle()).isEqualTo("Hello, World!");
    }

    @Test
    void delete() {
        // create post
        Post post = new Post();
        post.setTitle("Hello, World!");
        postRepository.save(post);
        // delete post
        postRepository.delete(post);
        // asser post cannot be found
        Post tmp = postRepository.findById(post.getPostId()).orElse(null);
        assertThat(tmp).isEqualTo(null);
    }

    @Test
    void findByDate() {
        // create 15 posts
        for (int i=0; i<15; i++) {
            Post post = new Post();
            post.setTitle("Post #" + i);
            postRepository.save(post);
        }
        // assert only 10 posts can be fetched
        List<Post> posts = postRepository.findByDate(new Date(), 10);
        assertThat(posts.size()).isEqualTo(10);

        // assert posts are sorted by date, in descending order (first = #14, second = #13, ...)
        List<Post> sorted =
                posts
                        .stream()
                        .sorted(
                                Comparator.comparing(Post::getDateCreated).reversed()
                        )
                        .collect(
                                Collectors.toList()
                        );
        for (int i=0; i<posts.size(); i++) {
            String a = posts.get(i).getTitle();
            String b = sorted.get(i).getTitle();
            assertThat(a).isEqualTo(b);
        }
    }

}
