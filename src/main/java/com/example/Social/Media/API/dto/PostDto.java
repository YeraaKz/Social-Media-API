package com.example.Social.Media.API.dto;

import com.example.Social.Media.API.entity.User;
import lombok.Data;

@Data
public class PostDto {

    private String title;

    private String text;

    private String image;

    private UserDto user;
}
