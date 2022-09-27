package com.mariodicaprio.mamba.entities;


import com.mariodicaprio.mamba.repositories.UserRepository;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * This class represents a user. Each user has the following properties:
 * <ul>
 *     <li>A User-ID (auto generated, unique)</li>
 *     <li>A Version (auto generated)</li>
 *     <li>A username (unique)</li>
 *     <li>An email</li>
 *     <li>A password</li>
 *     <li>A first name</li>
 *     <li>A last name</li>
 *     <li>A description</li>
 *     <li>A profile picture</li>
 *     <li>A list of followers</li>
 *     <li>A list of following users</li>
 *     <li>A list of all posts made by this user</li>
 * </ul>
 * @see UserRepository
 */
@Entity(name = "users")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString(onlyExplicitlyIncluded = true)
public class User {

    /**
     * Creates a user with basic registration data.
     * @param username The user's username
     * @param email The user's email
     * @param password The user's password
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /////////////////////////////////////////////////////////

    /**
     * The user's ID.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID userId;

    /////////////////////////////////////////////////////////

    /**
     * The user's version.
     */
    @Version
    @GeneratedValue
    @EqualsAndHashCode.Include
    private long userVersion;

    /////////////////////////////////////////////////////////

    /**
     * The user's username.
     */
    @Column(unique = true)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String username;

    /////////////////////////////////////////////////////////

    /**
     * The user's password (encoded).
     */
    @Column
    private String password;

    /////////////////////////////////////////////////////////

    /**
     * The user's email.
     */
    @Column
    private String email;

    /////////////////////////////////////////////////////////

    /**
     * The user's first name.
     */
    @Column
    private String firstName;

    /////////////////////////////////////////////////////////

    /**
     * The user's last name.
     */
    @Column
    private String lastName;

    /////////////////////////////////////////////////////////

    /**
     * The user's description.
     */
    @Column
    @Lob
    private String description;

    /////////////////////////////////////////////////////////

    /**
     * The user's profile picture.
     */
    @Column
    @Lob
    private byte[] profilePicture;

    /////////////////////////////////////////////////////////

    /**
     * The users that follow this user.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "following")
    private List<User> followers = new ArrayList<>();

    /////////////////////////////////////////////////////////

    /**
     * The users this user is following.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> following = new ArrayList<>();

    /////////////////////////////////////////////////////////

    /**
     * The posts that were posted by this user.
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "owner")
    private List<Post> posts = new ArrayList<>();

    /////////////////////////////////////////////////////////

    /**
     * The posts this user liked.
     */
    @JoinTable
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Post> likes = new ArrayList<>();

    /////////////////////////////////////////////////////////

    /**
     * This user's friends.
     */
    @JoinTable
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<User> friends = new ArrayList<>();

    /////////////////////////////////////////////////////////

    /**
     * The users that this user has sent friend requests to.
     */
    @JoinTable
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<User> sentFriendRequests = new ArrayList<>();

    /////////////////////////////////////////////////////////

    /**
     * The users that sent a friend request to this user.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<User> receivedFriendRequests = new ArrayList<>();

}
