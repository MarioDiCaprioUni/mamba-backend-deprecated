package com.mariodicaprio.mamba.repositories;


import com.mariodicaprio.mamba.entities.Media;
import com.mariodicaprio.mamba.entities.Post;
import com.mariodicaprio.mamba.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class PostRepositoryTests {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

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
    void findAllByUsername() {
        // create user and posts first
        User user = new User();
        user.setUsername("Hello");
        for (int i=0; i<5; i++) {
            var post = new Post();
            post.setOwner(user);
            user.getPosts().add(post);
        }
        userRepository.save(user);

        // try to fetch
        Pageable pageable = Pageable.ofSize(5);
        var page = postRepository.findAllByUsername(user.getUsername(), pageable);

        // assert all posts are contained
        assertThat(page.getTotalElements()).isEqualTo(5);
    }

    @Test
    void findAllPostsWithPictureByUsername() {
        // create user and posts first
        User user = new User();
        user.setUsername("Hello");

        Post picture = new Post();
        Media pictureMedia = new Media();
        pictureMedia.setType("image/png");
        picture.setType(Post.PostType.POST);
        picture.setMedia(pictureMedia);
        picture.setOwner(user);

        Post noPicture = new Post();
        noPicture.setType(Post.PostType.POST);
        noPicture.setOwner(user);

        user.getPosts().add(picture);
        user.getPosts().add(noPicture);

        userRepository.save(user);
        postRepository.save(picture);
        postRepository.save(noPicture);

        // try to fetch
        Pageable pageable = Pageable.ofSize(2);
        Page<Post> posts = postRepository.findAllPostsWithPictureByUsername(picture.getOwner().getUsername(), pageable);

        // assert only one post is contained
        assertThat(posts.getTotalElements()).isEqualTo(1);

        // expect element to be the picture
        Post post = posts.getContent().get(0);
        assertThat(post).isEqualTo(picture);
    }

    @Test
    void findAllPostsWithVideoByUsername() {
        // create user and posts first
        User user = new User();
        user.setUsername("Hello");

        Post video = new Post();
        Media pictureMedia = new Media();
        pictureMedia.setType("video/mp4");
        video.setType(Post.PostType.POST);
        video.setMedia(pictureMedia);
        video.setOwner(user);

        Post noVideo = new Post();
        noVideo.setType(Post.PostType.POST);
        noVideo.setOwner(user);

        user.getPosts().add(video);
        user.getPosts().add(noVideo);

        userRepository.save(user);
        postRepository.save(video);
        postRepository.save(noVideo);

        // try to fetch
        Pageable pageable = Pageable.ofSize(2);
        Page<Post> posts = postRepository.findAllPostsWithVideoByUsername(video.getOwner().getUsername(), pageable);

        // assert only one post is contained
        assertThat(posts.getTotalElements()).isEqualTo(1);

        // expect element to be the video
        Post post = posts.getContent().get(0);
        assertThat(post).isEqualTo(video);
    }

    @Test
    void findAllCommentsByUsername() {
        // create user and posts first
        User user = new User();
        user.setUsername("Hello");

        Post comment = new Post();
        comment.setType(Post.PostType.COMMENT);
        comment.setOwner(user);

        Post noComment = new Post();
        noComment.setType(Post.PostType.POST);
        noComment.setOwner(user);

        user.getPosts().add(comment);
        user.getPosts().add(noComment);

        userRepository.save(user);
        postRepository.save(comment);
        postRepository.save(noComment);

        // try to fetch
        Pageable pageable = Pageable.ofSize(2);
        Page<Post> posts = postRepository.findAllCommentsByUsername(comment.getOwner().getUsername(), pageable);

        // assert only one post is contained
        assertThat(posts.getTotalElements()).isEqualTo(1);

        // expect element to be the comment
        Post post = posts.getContent().get(0);
        assertThat(post).isEqualTo(comment);
    }

    @Test
    void findAllRepostsByUsername() {
        // create user and posts first
        User user = new User();
        user.setUsername("Hello");

        Post repost = new Post();
        repost.setType(Post.PostType.REPOST);
        repost.setOwner(user);

        Post noRepost = new Post();
        noRepost.setType(Post.PostType.POST);
        noRepost.setOwner(user);

        user.getPosts().add(repost);
        user.getPosts().add(noRepost);

        userRepository.save(user);
        postRepository.save(repost);
        postRepository.save(noRepost);

        // try to fetch
        Pageable pageable = Pageable.ofSize(2);
        Page<Post> posts = postRepository.findAllRepostsByUsername(repost.getOwner().getUsername(), pageable);

        // assert only one post is contained
        assertThat(posts.getTotalElements()).isEqualTo(1);

        // expect element to be the repost
        Post post = posts.getContent().get(0);
        assertThat(post).isEqualTo(repost);
    }

}
