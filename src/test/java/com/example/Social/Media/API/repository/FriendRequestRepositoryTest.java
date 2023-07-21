package com.example.Social.Media.API.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class FriendRequestRepositoryTest {

    private FriendRequestRepository friendRequestRepository;

    @Autowired
    public FriendRequestRepositoryTest(FriendRequestRepository friendRequestRepository){
        this.friendRequestRepository = friendRequestRepository;
    }

    @Test
    public void findByReceiverAndSender() {

    }

    @Test
    void existsBySenderAndReceiver() {
    }

    @Test
    void deleteBySenderAndReceiver() {
    }
}