package com.example.Social.Media.API.service;

import com.example.Social.Media.API.converter.UserConverter;
import com.example.Social.Media.API.dto.UserDto;
import com.example.Social.Media.API.entity.Role;
import com.example.Social.Media.API.entity.User;
import com.example.Social.Media.API.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldReturnAllUsersDto(){

        // Arrange
        List<User> userList = new ArrayList<>();

        userList.add(User.builder()
                .id(1L)
                .firstname("Yernar")
                .lastname("Orysbaeyev")
                .email("123@mail.ru")
                .password("123")
                .role(Role.USER)
                .build());

        userList.add(User.builder()
                .id(2L)
                .firstname("Rasul")
                .lastname("Kuantkhan")
                .email("321@mail.ru")
                .password("321")
                .role(Role.USER)
                .build());

        when(userRepository.findAll()).thenReturn(userList);

        List<UserDto> expectedUserDtos = userList.stream()
                .map(userConverter::convertEntityToDto)
                .collect(Collectors.toList());

        // Act
        List<UserDto> result = userService.findAll();

        // Assert
        assertEquals(expectedUserDtos, result);
    }

    @Test
    public void shouldReturnUserDtoById(){
        // Arrange
        User user = User.builder()
                .id(1L)
                .firstname("Yernar")
                .lastname("Orysbaeyev")
                .email("123@mail.ru")
                .password("123")
                .role(Role.USER)
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDto expectedUserDto = userConverter.convertEntityToDto(user);

        // Act
        UserDto result = userService.findById(user.getId());

        // Assert
        assertEquals(expectedUserDto, result);
    }

}
