package com.mariodicaprio.mamba.repositories;

import com.mariodicaprio.mamba.entities.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    /**
     * Finds posts given by the given maximum date. All selected dates
     * are older or just as old as the given date. The posts are ordered
     * by date in descending order.
     * @param maxDate The date the newest post was created at
     * @return The aforementioned list of posts
     */
    @Query(value = "SELECT * FROM Post p WHERE p.date_created >= :maxDate ORDER BY p.date_created DESC LIMIT :limit", nativeQuery = true)
    List<Post> findByDate(@Param("maxDate") Date maxDate, @Param("limit") int limit);

}
