package com.example.Social.Media.API.repository;

import com.example.Social.Media.API.entity.FriendRequest;
import com.example.Social.Media.API.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findByReceiverAndSender(User receiver, User sender);

    boolean existsBySenderAndReceiver(User receiver, User sender);

    void deleteBySenderAndReceiver(User receiver, User sender);
}
