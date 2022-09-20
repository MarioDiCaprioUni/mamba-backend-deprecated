package com.mariodicaprio.mamba.entities;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * This class represents a tag. A tag is used as an anchor to
 * a {@linkplain Post}, so to designate a topic. Each tag has the
 * following properties:
 * <ul>
 *     <li>A unique ID</li>
 *     <li>A version</li>
 *     <li>A name</li>
 *     <li>A list of posts that use this tag</li>
 * </ul>
 * @see Post
 */
@Entity
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString(onlyExplicitlyIncluded = true)
public class Tag {

    /**
     * The ID of this tag.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID tagId;

    //////////////////////////////////////////////////////////////////////////////

    /**
     * The version of this tag.
     */
    @Version
    @GeneratedValue
    @EqualsAndHashCode.Include
    private long tagVersion;

    //////////////////////////////////////////////////////////////////////////////

    /**
     * The name of this tag.
     */
    @Column(unique = true)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String name;

    //////////////////////////////////////////////////////////////////////////////

    /**
     * The posts that are associated with this tag.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "tags")
    private List<Post> posts = new ArrayList<>();

}
