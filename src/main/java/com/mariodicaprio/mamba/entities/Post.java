package com.mariodicaprio.mamba.entities;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * This class represents a post. A post can be made up of raw
 * text, but it can also contain an image. This entity comes
 * with the following features:
 * <ul>
 *     <li>A unique ID</li>
 *     <li>A version</li>
 *     <li>Date created</li>
 *     <li>Date updated</li>
 *     <li>A title</li>
 *     <li>An attached media</li>
 *     <li>Some text</li>
 *     <li>The user that owns this post</li>
 *     <li>The users that liked this post</li>
 *     <li>The posts that reference this post (such as comments)</li>
 *     <li>The post that this post references (e.g. if this post is a comment)</li>
 *     <li>A list of tags this post uses</li>
 *     <li>The type of this post</li>
 * </ul>
 * @see PostType
 * @see User
 */
@Entity
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString(onlyExplicitlyIncluded = true)
public class Post {

    /**
     * The post's ID.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @ToString.Include
    private UUID postId;

    //////////////////////////////////////////////////////////////

    /**
     * The post's version.
     */
    @Version
    @GeneratedValue
    @EqualsAndHashCode.Include
    private long postVersion;

    //////////////////////////////////////////////////////////////

    /**
     * The date this post was created on.
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column
    private Date dateCreated;

    //////////////////////////////////////////////////////////////

    /**
     * The date this post was last updated on.
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column
    private Date dateUpdated;

    //////////////////////////////////////////////////////////////

    /**
     * The post's title.
     */
    @Column
    @ToString.Include
    private String title;

    //////////////////////////////////////////////////////////////

    /**
     * The post's textual data.
     */
    @Lob
    @Column
    private String text;

    //////////////////////////////////////////////////////////////

    /**
     * The post's media (e.g. image or video).
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Media media;

    //////////////////////////////////////////////////////////////

    /**
     * The owner of this post.
     */
    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private User owner;

    //////////////////////////////////////////////////////////////

    /**
     * The users that liked this post.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "likes")
    private List<User> likes = new ArrayList<>();

    //////////////////////////////////////////////////////////////

    /**
     * The post that is referenced by this post (e.g. if this post is a comment to another post).
     */
    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Post reference;

    //////////////////////////////////////////////////////////////

    /**
     * The posts that reference this post (e.g. comments to this post).
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "reference")
    private List<Post> referencedBy = new ArrayList<>();

    //////////////////////////////////////////////////////////////

    /**
     * The tags associated with this post.
     */
    @JoinTable
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Tag> tags = new ArrayList<>();

    //////////////////////////////////////////////////////////////

    /**
     * The type of this post.
     */
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private PostType type = PostType.POST;

    //////////////////////////////////////////////////////////////


    /**
     * This enum represents the type of a post. Each post can be different,
     * such as a video, a single image or a simple comment.
     * @see Post
     */
    @SuppressWarnings("unused")
    public enum PostType {
        /**
         * The post is regular and does not reference any other post.
         */
        POST,
        /**
         * The post is a comment. No image, video or title are included.
         * This post is also completely textual.
         */
        COMMENT,
        /**
         * The post is a reference to another post. This post is completely
         * textual, but includes another post (which may be of a different type).
         */
        REPOST
    }

}
