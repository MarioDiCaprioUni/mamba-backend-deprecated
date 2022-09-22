package com.mariodicaprio.mamba.repositories;

import com.mariodicaprio.mamba.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * This is a JPA repository for the {@link Post} entity.
 * @see Post
 * @see JpaRepository
 */
@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    /**
     * Finds a post by its title.
     * @param title The post's title
     * @return The potential post
     */
    List<Post> findByTitle(String title);

    @Query("FROM Post post WHERE post.type = 'POST' AND post.media.type LIKE 'image%' AND post.owner.username = :username")
    Page<Post> findAllPostsWithPictureByUsername(@Param("username") String username, Pageable pageable);

    @Query("FROM Post post WHERE post.type = 'POST' AND post.media.type LIKE 'video%' AND post.owner.username = :username")
    Page<Post> findAllPostsWithVideoByUsername(@Param("username") String username, Pageable pageable);

    @Query("FROM Post post WHERE post.type = 'COMMENT' AND post.owner.username = :username")
    Page<Post> findAllCommentsByUsername(@Param("username") String username, Pageable pageable);

    @Query("FROM Post post WHERE post.type = 'REPOST' AND post.owner.username = :username")
    Page<Post> findAllRepostsByUsername(@Param("username") String username, Pageable pageable);

}
