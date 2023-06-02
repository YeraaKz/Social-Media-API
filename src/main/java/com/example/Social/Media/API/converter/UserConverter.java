package com.example.Social.Media.API.converter;

import com.example.Social.Media.API.dto.PostDto;
import com.example.Social.Media.API.dto.UserDto;
import com.example.Social.Media.API.entity.Post;
import com.example.Social.Media.API.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final ModelMapper modelMapper;

    public UserDto convertEntityToDto(User user){return modelMapper.map(user, UserDto.class);
    }

    public User convertDtoToEntity(UserDto userDto){return modelMapper.map(userDto, User.class);}
}
