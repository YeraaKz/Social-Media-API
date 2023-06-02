package com.example.Social.Media.API.rest;

import com.example.Social.Media.API.config.JwtAuthenticationFilter;
import com.example.Social.Media.API.converter.PostConverter;
import com.example.Social.Media.API.converter.UserConverter;
import com.example.Social.Media.API.dto.PostDto;
import com.example.Social.Media.API.dto.UserDto;
import com.example.Social.Media.API.entity.Post;
import com.example.Social.Media.API.entity.User;
import com.example.Social.Media.API.service.PostService;
import com.example.Social.Media.API.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/demo-controller")
@RequiredArgsConstructor
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
        List<User> users = userService.findAll();
        List<UserDto> userDtos = users.stream()
                .map(userConverter::convertEntityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<PostDto>> getUserPosts(@PathVariable("id") Long id){
        User user = userService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id :" + id + "was not found"));
        List<Post> userPosts = user.getPosts();
        List<PostDto> postDtos = userPosts.stream()
                .map(postConverter::convertEntityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts(){
        List<Post> posts = postService.findAll();
        List<PostDto> postDtos = posts.stream()
                .map(postConverter::convertEntityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDtos);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PostDto> editPost(@PathVariable("id") Long id, @RequestBody PostDto updatedPost){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByEmail(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this email was not found"));
        Post existingPost = postService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with this id: " + id + " was not found"));

        if(!existingPost.getUser().equals(currentUser)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission to update this post");
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setText(updatedPost.getText());
        existingPost.setImage(updatedPost.getImage());

        return ResponseEntity.ok(postService.save(existingPost));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Long> deletePost(@PathVariable("id") Long id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByEmail(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this email was not found"));
        Post postToDelete = postService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with this id: " + id + " was not found"));

        if(!postToDelete.getUser().equals(currentUser)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission to delete this post");
        }

        postService.delete(postToDelete);

        return ResponseEntity.ok(id);
    }

    @PostMapping("/posts/addPost")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByEmail(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this email was not found"));
        return ResponseEntity.ok(postService.save(postDto, currentUser));
    }
}
