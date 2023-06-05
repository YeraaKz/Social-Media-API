package com.example.Social.Media.API.service;

import com.example.Social.Media.API.entity.FriendRequest;
import com.example.Social.Media.API.entity.User;
import com.example.Social.Media.API.repository.FriendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    public FriendRequest findByReceiverAndSender(User receiver, User sender) {
        return friendRequestRepository.findByReceiverAndSender(receiver, sender).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Friend request not found")
        );
    }

    public boolean existsBySenderAndReceiver(User receiver, User sender) {
        return friendRequestRepository.existsBySenderAndReceiver(receiver, sender);
    }

    @Transactional
    public void save(FriendRequest friendRequest) {
        friendRequestRepository.save(friendRequest);
    }

    @Transactional
    public void delete(FriendRequest friendRequest) {
        friendRequestRepository.delete(friendRequest);
    }

    @Transactional
    public void deleteBySenderAndReceiver(User receiver, User sender) {
        friendRequestRepository.deleteBySenderAndReceiver(receiver,sender);
    }
}
