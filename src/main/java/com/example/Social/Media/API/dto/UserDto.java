package com.example.Social.Media.API.dto;

import com.example.Social.Media.API.entity.Role;
import lombok.*;

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
