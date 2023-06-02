package com.example.Social.Media.API.dto;

import com.example.Social.Media.API.entity.Role;
import com.example.Social.Media.API.entity.User;
import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String firstname;

    private String lastname;

    private String email;

    private Role role;

}
