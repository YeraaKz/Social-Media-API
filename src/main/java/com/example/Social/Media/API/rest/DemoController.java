package com.example.Social.Media.API.rest;

import com.example.Social.Media.API.config.JwtAuthenticationFilter;
import com.example.Social.Media.API.converter.PostConverter;
import com.example.Social.Media.API.converter.UserConverter;
import com.example.Social.Media.API.dto.FriendRequestResponse;
import com.example.Social.Media.API.dto.PostDto;
import com.example.Social.Media.API.dto.UserDto;
import com.example.Social.Media.API.entity.FriendRequest;
import com.example.Social.Media.API.entity.Post;
import com.example.Social.Media.API.entity.User;
import com.example.Social.Media.API.service.PostService;
import com.example.Social.Media.API.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/demo-controller")
@RequiredArgsConstructor
@Slf4j
public class DemoController {

    private final PostService postService;

    private final UserService userService;

    private final UserConverter userConverter;

    public final PostConverter postConverter;

    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello from secured App");
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers(){

        List<UserDto> users = userService.findAll();

        log.info("DemoController::getAllUsers response {}", users);

        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> userInfo(@PathVariable("id") Long id){

        log.info("DemoController::userInfo by id {}", id);

        UserDto userDto = userService.findById(id);

        log.info("DemoController::userInfo by id {} response {}", id, userDto);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/users/{id}")
    public ResponseEntity<String> sendFriendRequest(@PathVariable("id") Long id){

        log.info("DemoController::sendFriendRequest to id {}", id);
        FriendRequestResponse response = userService.sendFriendRequest(id);

        switch (response){
            case REQUEST_SEND:
                return ResponseEntity.ok("Friend Request send successfully");
            case FRIENDSHIP_ESTABILISHED:
                return ResponseEntity.ok("Friendship established successfully");
            case ALREADY_FRIENDS:
                return ResponseEntity.ok("Users are already friends");
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

    @GetMapping("/users/{id}/receivedFriendRequests")
    public ResponseEntity<List<UserDto>> getFriendRequests(@PathVariable("id")Long id){

        log.info("DemoController::getFriendRequests by id {}", id);
        return ResponseEntity.ok(userService.getFriendRequests(id));
    }

    @PostMapping("/users/{id}/receivedFriendRequests/{senderId}/accept")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable("id") Long receiverId,
                                                      @PathVariable("senderId") Long senderId){
        userService.acceptFriendRequest(receiverId, senderId);

        return ResponseEntity.ok("Friend request accepted");
    }

    @PostMapping("/users/{id}/receivedFriendRequests/{senderId}/decline")
    public ResponseEntity<String> declineFriendRequests(@PathVariable("id") Long receiverId,
                                                        @PathVariable("senderId") Long senderId){

        userService.declineFriendRequest(receiverId, senderId);

        return ResponseEntity.ok("Friend request declined");
    }

    @GetMapping("/users/{id}/friendList")
    public ResponseEntity<Set<UserDto>> getUserFriends(@PathVariable("id") Long id){
        return ResponseEntity.ok(userService.getUserFriends(id));
    }

    @DeleteMapping("/users/{id}/friendList/{userToBeDeletedId}")
    public ResponseEntity<String> deleteFromFriends(@PathVariable("id") Long id,
                                               @PathVariable("userToBeDeletedId") Long userToBeDeletedId){
        userService.deleteFromFriends(id, userToBeDeletedId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User successfully removed from friend list.");
    }


    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<PostDto>> getUserPosts(@PathVariable("id") Long id){
        return ResponseEntity.ok(userService.getUserPosts(id));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts(){
        return ResponseEntity.ok(postService.findAll());
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto){
        return ResponseEntity.ok(postService.createPost(postDto));
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PostDto> editPost(@PathVariable("id") Long id, @RequestBody PostDto updatedPost){
        return ResponseEntity.ok(postService.update(id, updatedPost));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Long> deletePost(@PathVariable("id") Long id){
        return ResponseEntity.ok(postService.delete(id));
    }
}
