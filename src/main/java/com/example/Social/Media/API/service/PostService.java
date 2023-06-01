package com.example.Social.Media.API.service;

import com.example.Social.Media.API.model.Post;
import com.example.Social.Media.API.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> findAll(){
        return postRepository.findAll();
    }
}
