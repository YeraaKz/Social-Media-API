package com.example.Social.Media.API.service;

import com.example.Social.Media.API.converter.PostConverter;
import com.example.Social.Media.API.dto.PostDto;
import com.example.Social.Media.API.entity.Post;
import com.example.Social.Media.API.entity.User;
import com.example.Social.Media.API.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    private final PostConverter postConverter;

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public Optional<Post> findById(Long id) {return postRepository.findById(id);}

    @Transactional
    public PostDto save(Post post){
        postRepository.save(post);
        return postConverter.convertEntityToDto(post);
    }

    @Transactional
    public PostDto save(PostDto postDto, User user){
        Post post = postConverter.convertDtoToEntity(postDto);
        post.setUser(user);
        postRepository.save(post);
        return postConverter.convertEntityToDto(post);
    }

    @Transactional
    public void delete(Post postToDelete) {
        postRepository.delete(postToDelete);
    }
}
