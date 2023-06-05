package com.example.Social.Media.API.service;

import com.example.Social.Media.API.converter.PostConverter;
import com.example.Social.Media.API.dto.PostDto;
import com.example.Social.Media.API.entity.Post;
import com.example.Social.Media.API.entity.User;
import com.example.Social.Media.API.exception.PostNotFoundException;
import com.example.Social.Media.API.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    private final UserService userService;

    private final PostConverter postConverter;


    public List<PostDto> findAll(){
        return postRepository.findAll().stream()
                .map(post -> postConverter.convertEntityToDto(post))
                .collect(Collectors.toList());
    }

    public Optional<Post> findById(Long id) {return postRepository.findById(id);}


    @Transactional
    public PostDto createPost(PostDto postDto){

        User currentUser = userService.getCurrentUser();
        userService.checkAccess(currentUser.getId());

        Post post = postConverter.convertDtoToEntity(postDto);
        post.setUser(currentUser);

        postRepository.save(post);

        return postConverter.convertEntityToDto(post);
    }

    @Transactional
    public PostDto update(Long id, PostDto updatedPost){
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id " + id));

        userService.checkAccess(existingPost.getUser().getId());

        existingPost.builder()
                .title(updatedPost.getTitle())
                .text(updatedPost.getText())
                .image(updatedPost.getImage())
                .build();

        return postConverter.convertEntityToDto(existingPost);
    }

    @Transactional
    public Long delete(Long id) {
        Post postToDelete = postRepository.findById(id)
                        .orElseThrow(() -> new PostNotFoundException("Post not found with id " + id ));
        postRepository.delete(postToDelete);
        return id;
    }
}
