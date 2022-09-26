package com.mariodicaprio.mamba.services;


import com.mariodicaprio.mamba.entities.Post;
import com.mariodicaprio.mamba.entities.User;
import com.mariodicaprio.mamba.exceptions.DuplicateFriendRequestException;
import com.mariodicaprio.mamba.exceptions.UserDoesNotExistException;
import com.mariodicaprio.mamba.repositories.PostRepository;
import com.mariodicaprio.mamba.repositories.UserRepository;
import com.mariodicaprio.mamba.requests.FriendRequest;
import com.mariodicaprio.mamba.requests.LikePostRequest;
import com.mariodicaprio.mamba.responses.UserBasicDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


/**
 * This service handles data related to the {@link User} entity.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    /////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a {@link Page} of all users at the given page. The page is unsorted and of
     * size 15. The page index is one-based.
     * @param page The index of the requested page (one-based)
     * @return The corresponding page of users.
     */
    @Transactional(readOnly = true)
    public Page<User> all(int page) {
        Sort sort = Sort.unsorted();
        Pageable pageable = PageRequest.of(page - 1, 15, sort);
        return userRepository.findAll(pageable);
    }

    /**
     * Returns the user with the given ID. If no user was found, {@code null}
     * is returned instead.
     * @param userId The ID of the user to fetch
     * @return The user with the given ID or {@code null} of not found
     */
    @Transactional(readOnly = true)
    public User byId(UUID userId) {
        if (userId == null)
            return null;
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * Returns the user with the given username. If no user was found, {@code null}
     * is returned instead.
     * @param username The username of the user to fetch
     * @return The user with the given username
     */
    @Transactional(readOnly = true)
    public User byUsername(String username) {
        if (username == null)
            return null;
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Commits a like action from a user's part.
     * @param request The like request
     * @see LikePostRequest
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void likePost(LikePostRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) return;

        Post post = postRepository.findById(request.getPostId()).orElse(null);
        if (post == null) return;

        boolean userLikesPost = user.getLikes().contains(post);
        if (userLikesPost && !request.isLike()) {
            // if user already likes post and wants to unlike
            user.getLikes().remove(post);
            post.getLikes().remove(user);
            userRepository.save(user);
        } else if (!userLikesPost && request.isLike()) {
            // if user does not like post yet and wants to like
            user.getLikes().add(post);
            post.getLikes().add(user);
            userRepository.save(user);
        }
    }

    /**
     * Retrieves a user's basic data given their ID.
     * @param userId The user's ID
     * @return The user's basic data
     * @see UserBasicDataResponse
     */
    @Transactional(readOnly = true)
    public UserBasicDataResponse basicData(UUID userId) {
        User user = this.byId(userId);
        return (user == null)? null : new UserBasicDataResponse(user);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void sendFriendRequest(FriendRequest request) throws DuplicateFriendRequestException, UserDoesNotExistException {
        User from = this.byId(request.getFrom());
        User to = this.byId(request.getTo());

        if (from == null || to == null) {
            throw new UserDoesNotExistException("This user does not exist!");
        }

        if (from.getFriends().contains(to)) {
            throw new DuplicateFriendRequestException("This user is already a friend!");
        }

        if (from.getSentFriendRequests().contains(to) || from.getReceivedFriendRequests().contains(to)) {
            throw new DuplicateFriendRequestException("This friend request is already pending!");
        }

        from.getSentFriendRequests().add(to);
        to.getReceivedFriendRequests().add(from);

        userRepository.save(from);
        userRepository.save(to);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void acceptFriendRequest(FriendRequest request) throws UserDoesNotExistException {
        User from = this.byId(request.getFrom());
        User to = this.byId(request.getTo());

        if (from == null || to == null) {
            throw new UserDoesNotExistException("This user does not exist!");
        }

        from.getSentFriendRequests().remove(to);
        from.getFriends().add(to);
        to.getReceivedFriendRequests().remove(from);
        to.getFriends().add(from);

        userRepository.save(from);
        userRepository.save(to);
    }

}
