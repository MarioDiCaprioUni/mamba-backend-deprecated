package com.mariodicaprio.mamba.services;


import com.mariodicaprio.mamba.entities.Media;
import com.mariodicaprio.mamba.entities.Post;
import com.mariodicaprio.mamba.entities.Tag;
import com.mariodicaprio.mamba.entities.User;
import com.mariodicaprio.mamba.repositories.PostRepository;
import com.mariodicaprio.mamba.requests.CreateCommentRequest;
import com.mariodicaprio.mamba.requests.CreatePostRequest;
import com.mariodicaprio.mamba.requests.CreateRepostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    TagService tagService;

    @Autowired
    UserService userService;

    ///////////////////////////////////////////////////////////////////////////////

    public Page<Post> all(int page) {
        Sort sort = Sort.by("dateCreated").descending();
        Pageable pageable = PageRequest.of(page - 1, 15, sort);
        return postRepository.findAll(pageable);
    }

    public Post byId(UUID postId) {
        if (postId == null)
            return null;
        return postRepository.findById(postId).orElse(null);
    }

    @Transactional
    public void createPost(CreatePostRequest request) {
        List<Tag> tags =
                (request.getTagNames() == null)? new ArrayList<>() :
                        request
                        .getTagNames()
                        .stream()
                        .map(tagName -> tagService.tagByName(tagName))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toCollection(ArrayList::new));

        User owner = (request.getOwnerId() == null)? null : userService.byId(request.getOwnerId());

        Media media = null;
        if (request.getMedia() != null) {
            media = new Media();
            media.setData(request.getMedia().getData());
            media.setType(request.getMedia().getType());
        }

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        post.setMedia(media);
        post.setOwner(owner);
        post.setTags(tags);
        post.setType(Post.PostType.POST);
        postRepository.save(post);
    }

    @Transactional
    public void createComment(CreateCommentRequest request) {
        User owner = userService.byId(request.getOwnerId());
        Post reference = this.byId(request.getReferenceId());

        Post post = new Post();
        post.setText(request.getText());
        post.setOwner(owner);
        post.setReference(reference);
        postRepository.save(post);
    }

    @Transactional
    public void createRepost(CreateRepostRequest request) {
        List<Tag> tags =
                (request.getTagNames() == null)? new ArrayList<>() :
                        request
                        .getTagNames()
                        .stream()
                        .map(tagName -> tagService.tagByName(tagName))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toCollection(ArrayList::new));

        User owner = userService.byId(request.getOwnerId());
        Post reference = this.byId(request.getReferenceId());

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        post.setOwner(owner);
        post.setReference(reference);
        post.setTags(tags);
        postRepository.save(post);
    }

    @Transactional
    public Page<Post> byUsername(String username, int page) {
        Sort sort = Sort.by("dateCreated").descending();
        Pageable pageable = PageRequest.of(page - 1, 15, sort);
        return postRepository.findAllByUsername(username, pageable);
    }

    @Transactional
    public Page<Post> picturePostsByUsername(String username, int page) {
        Sort sort = Sort.by("dateCreated").descending();
        Pageable pageable = PageRequest.of(page - 1, 15, sort);
        return postRepository.findAllPostsWithPictureByUsername(username, pageable);
    }

    @Transactional
    public Page<Post> videoPostsByUsername(String username, int page) {
        Sort sort = Sort.by("dateCreated").descending();
        Pageable pageable = PageRequest.of(page - 1, 15, sort);
        return postRepository.findAllPostsWithVideoByUsername(username, pageable);
    }

    @Transactional
    public Page<Post> commentsByUsername(String username, int page) {
        Sort sort = Sort.by("dateCreated").descending();
        Pageable pageable = PageRequest.of(page - 1, 15, sort);
        return postRepository.findAllCommentsByUsername(username, pageable);
    }

    @Transactional
    public Page<Post> repostsByUsername(String username, int page) {
        Sort sort = Sort.by("dateCreated").descending();
        Pageable pageable = PageRequest.of(page - 1, 15, sort);
        return postRepository.findAllRepostsByUsername(username, pageable);
    }

}
