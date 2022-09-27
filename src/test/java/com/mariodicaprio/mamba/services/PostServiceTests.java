package com.mariodicaprio.mamba.services;


import com.google.common.collect.Ordering;
import com.mariodicaprio.mamba.entities.Media;
import com.mariodicaprio.mamba.entities.Post;
import com.mariodicaprio.mamba.entities.User;
import com.mariodicaprio.mamba.repositories.MediaRepository;
import com.mariodicaprio.mamba.repositories.PostRepository;
import com.mariodicaprio.mamba.repositories.UserRepository;
import org.jeasy.random.EasyRandom;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    EasyRandom easyRandom;

    ///////////////////////////////////////////////////////////////////////

    @Test
    void postAll() throws InterruptedException {
        // create user first
        User user = easyRandom.nextObject(User.class);
        userRepository.save(user);

        // create media first
        Media media = easyRandom.nextObject(Media.class);
        mediaRepository.save(media);

        // create 20 posts
        for (int i=0; i<20; i++) {
            Post post = new Post();
            post.setTitle("Post #" + i);
            post.setOwner(user);
            post.setMedia(media);
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

    ///////////////////////////////////////////////////////////////////////

    @Test
    void byUsername() {
        // create user and posts first
        User user = easyRandom.nextObject(User.class);
        user.setUsername("Hello");
        for (int i=0; i<20; i++) {
            var post = new Post();
            post.setOwner(user);
            user.getPosts().add(post);
        }
        userRepository.save(user);

        // try to fetch
        var page = postService.byUsername(user.getUsername(), 1);

        // assert size of page is 15
        assertThat(page.getSize()).isEqualTo(15);

        // assert total elements is 15
        assertThat(page.getContent().size()).isEqualTo(15);

        // assert 20 posts were found in total
        assertThat(page.getTotalElements()).isEqualTo(20);
    }

    ///////////////////////////////////////////////////////////////////////

    @Test
    void picturesByUsername() {
        // create user and posts first
        User user = new User();
        user.setUsername("Hello");

        for (int i=0; i<20; i++) {
            Media media = new Media();
            media.setType("image/png");

            Post picture = new Post();
            picture.setMedia(media);
            picture.setOwner(user);

            Post noPicture = new Post();
            noPicture.setOwner(user);

            user.getPosts().add(picture);
            user.getPosts().add(noPicture);
        }

        userRepository.save(user);

        // try to fetch
        var page = postService.picturePostsByUsername(user.getUsername(), 1);

        // assert size of page is 15
        assertThat(page.getSize()).isEqualTo(15);

        // assert total elements is 15
        assertThat(page.getContent().size()).isEqualTo(15);

        // assert 20 posts were found in total
        assertThat(page.getTotalElements()).isEqualTo(20);

        // assert all posts contain an image
        page.getContent().forEach(post -> {
            assertThat(post.getMedia()).isNotNull();
            assertThat(post.getMedia().getType()).startsWith("image");
        });
    }

    ///////////////////////////////////////////////////////////////////////

    @Test
    void videosByUsername() {
        // create user and posts first
        User user = new User();
        user.setUsername("Hello");

        for (int i=0; i<20; i++) {
            Media media = new Media();
            media.setType("video/mp4");

            Post video = new Post();
            video.setMedia(media);
            video.setOwner(user);

            Post noVideo = new Post();
            noVideo.setOwner(user);

            user.getPosts().add(video);
            user.getPosts().add(noVideo);
        }

        userRepository.save(user);

        // try to fetch
        var page = postService.videoPostsByUsername(user.getUsername(), 1);

        // assert size of page is 15
        assertThat(page.getSize()).isEqualTo(15);

        // assert total elements is 15
        assertThat(page.getContent().size()).isEqualTo(15);

        // assert 20 posts were found in total
        assertThat(page.getTotalElements()).isEqualTo(20);

        // assert all posts contain a video
        page.getContent().forEach(post -> {
            assertThat(post.getMedia()).isNotNull();
            assertThat(post.getMedia().getType()).startsWith("video");
        });
    }

    ///////////////////////////////////////////////////////////////////////

    @Test
    void commentsByUsername() {
        // create user and posts first
        User user = new User();
        user.setUsername("Hello");

        for (int i=0; i<20; i++) {
            Post comment = new Post();
            comment.setType(Post.PostType.COMMENT);
            comment.setOwner(user);

            Post noComment = new Post();
            noComment.setType(Post.PostType.POST);
            noComment.setOwner(user);

            user.getPosts().add(comment);
            user.getPosts().add(noComment);
        }

        userRepository.save(user);

        // try to fetch
        var page = postService.commentsByUsername(user.getUsername(), 1);

        // assert size of page is 15
        assertThat(page.getSize()).isEqualTo(15);

        // assert total elements is 15
        assertThat(page.getContent().size()).isEqualTo(15);

        // assert 20 posts were found in total
        assertThat(page.getTotalElements()).isEqualTo(20);

        // assert all posts are comments
        page.getContent().forEach(post -> assertThat(post.getType()).isEqualTo(Post.PostType.COMMENT));
    }

    ///////////////////////////////////////////////////////////////////////

    @Test
    void repostsByUsername() {
        // create user and posts first
        User user = new User();
        user.setUsername("Hello");

        for (int i=0; i<20; i++) {
            Post repost = new Post();
            repost.setType(Post.PostType.REPOST);
            repost.setOwner(user);

            Post noRepost = new Post();
            noRepost.setType(Post.PostType.POST);
            noRepost.setOwner(user);

            user.getPosts().add(repost);
            user.getPosts().add(noRepost);
        }

        userRepository.save(user);

        // try to fetch
        var page = postService.repostsByUsername(user.getUsername(), 1);

        // assert size of page is 15
        assertThat(page.getSize()).isEqualTo(15);

        // assert total elements is 15
        assertThat(page.getContent().size()).isEqualTo(15);

        // assert 20 posts were found in total
        assertThat(page.getTotalElements()).isEqualTo(20);

        // assert all posts are reposts
        page.getContent().forEach(post -> assertThat(post.getType()).isEqualTo(Post.PostType.REPOST));
    }

}
