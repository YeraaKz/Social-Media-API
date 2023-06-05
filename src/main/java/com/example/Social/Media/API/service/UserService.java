package com.example.Social.Media.API.service;

import com.example.Social.Media.API.converter.PostConverter;
import com.example.Social.Media.API.converter.UserConverter;
import com.example.Social.Media.API.dto.FriendRequestResponse;
import com.example.Social.Media.API.dto.PostDto;
import com.example.Social.Media.API.dto.UserDto;
import com.example.Social.Media.API.entity.FriendRequest;
import com.example.Social.Media.API.entity.Post;
import com.example.Social.Media.API.entity.User;
import com.example.Social.Media.API.exception.UserNotFoundException;
import com.example.Social.Media.API.exception.UserServiceBusinessException;
import com.example.Social.Media.API.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    private final UserConverter userConverter;

    private final PostConverter postConverter;

    private final FriendRequestService friendRequestService;

    public List<UserDto> findAll(){
        List<UserDto> userDtos = null;

        try{
            log.info("UserService::findAll execution started.");

            List<User> userList = userRepository.findAll();

            if(!userList.isEmpty()){
                userDtos = userList.stream()
                        .map(userConverter::convertEntityToDto)
                        .collect(Collectors.toList());
            }
            else{
                userDtos = Collections.EMPTY_LIST;
            }

            log.debug("UserService::findAll retrieving users from database {}", userDtos);

        }catch (Exception e){
            log.error("Exception occurred while retrieving users from database, Exception message {}", e.getMessage());
            throw new UserServiceBusinessException("Exception occurred while fetch all users from Database ");
        }

        log.info("UserService::findAll execution ended.");
        return userDtos;
    }

    public UserDto findById(Long id) {
        UserDto userDto;

        try{

            log.info("UserService::findById execution started.");
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
            userDto = userConverter.convertEntityToDto(user);

            log.debug("UserService::findById retrieving user from database for id {} {}", id, userDto);

        }catch (Exception e){
            log.error("Exception occurred while retrieving user {} from database , Exception message {}",
                    id, e.getMessage());
            throw new UserServiceBusinessException("Exception occurred while fetch user from Database " + id);
        }

        log.info("UserService::findById execution ended.");
        return userDto;
    }

    public List<PostDto> getUserPosts(Long id) {
        List<PostDto> postDtos;

        try{

            log.info("UserService::getUserPosts execution started.");

            User user = userRepository.findById(id).
                    orElseThrow(() -> new UserNotFoundException(("User not found with id " + id)));
            List<Post> userPosts = user.getPosts();
            postDtos = userPosts.stream()
                    .map(postConverter::convertEntityToDto)
                    .collect(Collectors.toList());

            log.debug("UserService::getUserPosts retrieving user posts from database for id {} {}", id, postDtos);

        }catch (Exception e){
            log.error("Exception occurred while retrieving posts of user from database, Exception message {}",
                    id, e.getMessage());
            throw new UserServiceBusinessException("Exception occurred while fetching user posts from Database " + id);
        }

        log.info("UserService::getUserPosts execution ended.");
        return postDtos;
    }


    public List<UserDto> getFriendRequests(Long id) {
        List<UserDto> friendRequestSenderList;

        try{

            log.info("UserService::getFriendRequests execution started.");

            checkAccess(id);

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

            List<FriendRequest> friendRequestList = user.getFriendRequests();
            friendRequestSenderList = new ArrayList<>();

            for(FriendRequest request : friendRequestList){
                User requestSender = userRepository.findById(request.getSender().getId())
                        .orElseThrow(() -> new UserNotFoundException("User not found with id " + request.getSender().getId()));
                friendRequestSenderList.add(userConverter.convertEntityToDto(requestSender));
            }

            log.debug("UserService::getFriendRequests retrieving user friend requests from database for id {} {}", id,
                    friendRequestSenderList);

        }catch (Exception e){
            log.error("Exception occurred while retrieving friend requests of user from database, Exception message {}",
                    id, e.getMessage());
            throw new UserServiceBusinessException("Exception occurred while fetching user friend requests from Database " + id);
        }

        log.info("UserService::getFriendRequests execution ended.");
        return friendRequestSenderList;
    }


    public Set<UserDto> getUserFriends(Long id) {
        Set<UserDto> friendListDto;

        try {

            log.info("UserService::getUserFriends execution started.");

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

            Set<User> friendList = user.getFriends();

            friendListDto = friendList.stream()
                    .map(userConverter::convertEntityToDto)
                    .collect(Collectors.toSet());

            log.debug("UserService::getUserFriends retrieving user friends from database for id {} {}", id,
                    friendListDto);

        }catch (Exception e){
            log.error("Exception occurred while retrieving friends of user from database, Exception message {}",
                    id, e.getMessage());
            throw new UserServiceBusinessException("Exception occurred while fetching user friends from Database " + id);
        }
        log.info("UserService::getUserFriends execution ended.");
        return friendListDto;
    }

    @Transactional
    public FriendRequestResponse sendFriendRequest(Long receiverId) {

        User sender = getCurrentUser();
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + receiverId));

        if(!receiver.getFriends().contains(sender) || !sender.getFriends().contains(receiver)){
            if(friendRequestService.existsBySenderAndReceiver(receiver, sender)){

                receiver.getFriends().add(sender);
                sender.getFriends().add(receiver);

                receiver.getSubscribers().add(sender);

                userRepository.save(receiver);
                userRepository.save(sender);

                friendRequestService.deleteBySenderAndReceiver(receiver, sender);

                userRepository.save(receiver);

                userRepository.save(sender);

                return FriendRequestResponse.FRIENDSHIP_ESTABILISHED;
            }
            else{

                receiver.getSubscribers().add(sender);

                FriendRequest friendRequest = FriendRequest.builder()
                        .sender(sender)
                        .receiver(receiver)
                        .build();
                friendRequestService.save(friendRequest);

                return FriendRequestResponse.REQUEST_SEND;
            }
        }
        else{

            return FriendRequestResponse.ALREADY_FRIENDS;
        }
    }

    @Transactional
    public void acceptFriendRequest(Long receiverId, Long senderId) {

        checkAccess(receiverId);

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + receiverId));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + senderId));

        FriendRequest friendRequest = friendRequestService.findByReceiverAndSender(receiver, sender);

        receiver.getFriends().add(sender);

        sender.getSubscribers().add(receiver);

        userRepository.save(sender);
        userRepository.save(receiver);

        friendRequestService.delete(friendRequest);

    }

    @Transactional
    public void declineFriendRequest(Long receiverId, Long senderId) {

        checkAccess(receiverId);

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + receiverId));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id "+ senderId));

        FriendRequest friendRequest = friendRequestService.findByReceiverAndSender(receiver, sender);

        friendRequestService.delete(friendRequest);
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with email " + authentication.getName()));
        return currentUser;
    }

    public  void checkAccess(Long id){
        if(!getCurrentUser().getId().equals(id)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access forbidden");
        }
    }

    @Transactional
    public void deleteFromFriends(Long id, Long userToBeDeletedId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

        User userToBeDeleted = userRepository.findById(userToBeDeletedId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userToBeDeletedId));

        if(user.getFriends().contains(userToBeDeleted)) {
            user.getFriends().remove(userToBeDeleted);
            userToBeDeleted.getFriends().remove(user);

            user.getSubscribers().remove(userToBeDeleted);

            userRepository.save(user);
            userRepository.save(userToBeDeleted);
        }
        else{
            throw new IllegalArgumentException("User " + userToBeDeleted + " is not a friend of User " + id);
        }
    }
}
