package com.example.Social.Media.API.converter;

import com.example.Social.Media.API.dto.PostDto;
import com.example.Social.Media.API.entity.Post;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostConverter {

    private final ModelMapper modelMapper;

    public PostDto convertEntityToDto(Post post){
        return modelMapper.map(post, PostDto.class);
    }

    public Post convertDtoToEntity(PostDto postDto){
        return modelMapper.map(postDto, Post.class);
    }
}
