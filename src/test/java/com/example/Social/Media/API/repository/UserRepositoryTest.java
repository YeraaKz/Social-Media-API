package com.example.Social.Media.API.repository;

import com.example.Social.Media.API.entity.Role;
import com.example.Social.Media.API.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    private UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Test
    public void UserRepository_Save_ReturnSavedUser(){

        // Arrange
        User user = User.builder()
                .firstname("Yernar")
                .lastname("Orysbaev")
                .email("testemail@example.com")
                .password("123321")
                .role(Role.USER)
                .build();

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepository_FindAll_ReturnMoreThanOneUser(){

        // Arrange
        User user1 = User.builder()
                .firstname("Yernar")
                .lastname("Orysbaev")
                .email("testemail@example.com")
                .password("123321")
                .role(Role.USER)
                .build();

        User user2 = User.builder()
                .firstname("Yernar")
                .lastname("Orysbaev")
                .email("testemail@example.com")
                .password("123321")
                .role(Role.USER)
                .build();

        // Act

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> userList = userRepository.findAll();

        // Assert
        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    public void UserRepository_FindById_ReturnUser(){

        // Arrange
        User user = User.builder()
                .firstname("Yernar")
                .lastname("Orysbaev")
                .email("testemail@example.com")
                .password("123321")
                .role(Role.USER)
                .build();

        // Act

        userRepository.save(user);

        User foundUser = userRepository.findById(user.getId()).get();

        // Assert

        assertThat(foundUser).isNotNull();

    }

    @Test
    public void UserRepository_FindByEmail_ReturnUserNotNull(){
        User user = User.builder()
                .firstname("Yernar")
                .lastname("Orysbaev")
                .email("testemail@example.com")
                .password("123321")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        User foundUser = userRepository.findByEmail(user.getEmail()).get();

        assertThat(foundUser).isNotNull();

    }

    @Test
    public void UserRepository_UpdateUser_ReturnUserNotNull(){
        User user = User.builder()
                .firstname("Yernar")
                .lastname("Orysbaev")
                .email("testemail@example.com")
                .password("123321")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        User userToUpdate = userRepository.findById(user.getId()).get();

        userToUpdate.setEmail("newEmail@example.com");
        userToUpdate.setFirstname("newFirstname");
        userToUpdate.setLastname("newLastname");
        userToUpdate.setPassword("newPassword");
        userToUpdate.setRole(Role.ADMIN);

        User updatedUser = userRepository.save(userToUpdate);

        assertThat(updatedUser.getFirstname()).isNotNull();
        assertThat(updatedUser.getLastname()).isNotNull();
        assertThat(updatedUser.getEmail()).isNotNull();
        assertThat(updatedUser.getPassword()).isNotNull();
        assertThat(updatedUser.getRole()).isNotNull();

    }

    @Test
    public void UserRepository_DeleteUser_ReturnUserIsEmpty(){
        User user = User.builder()
                .firstname("Yernar")
                .lastname("Orysbaev")
                .email("testemail@example.com")
                .password("123321")
                .role(Role.USER)
                .build();

        userRepository.save(user);


        userRepository.deleteById(user.getId());
        Optional<User> deletedUser = userRepository.findById(user.getId());

        assertThat(deletedUser).isEmpty();

    }



}
