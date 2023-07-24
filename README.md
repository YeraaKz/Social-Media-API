# Social-Media-API

The Social Media API is a RESTful web service that provides endpoints to manage user profiles, posts, friendships, and chat functionality.

## Prerequisites

To run the Social Media API, you need to have the following installed on your system:

- Java Development Kit (JDK) 11 or higher
- Apache Maven
- PostgreSQL Server

## Getting Started

Follow these steps to get the Social Media API up and running on your local machine:

1. Clone the repository:

   ```shell
   git clone https://github.com/YeraaKz/Social-Media-API.git
   
2. Configure the database:

  Create a PostgreSQL database for the application.
  Update the database connection details in the application.properties file located in the src/main/resources directory

3. Build the application: 

  cd Social-Media-API
  mvn clean install

4. Run the application:

  mvn spring-boot:run
  
The API will start running on http://localhost:8080.

## API Documentation

Get a list of all users
GET /api/v1/demo-controller/users

Get user information by ID
GET /api/v1/demo-controller/users/{id}

Send a friend request to a user
POST /api/v1/demo-controller/users/{id}

Get a list of friend requests received by a user
GET /api/v1/demo-controller/users/{id}/receivedFriendRequests

Accept a friend request from a specific user
POST /api/v1/demo-controller/users/{id}/receivedFriendRequests/{senderId}/accept

Decline a friend request from a specific user
POST /api/v1/demo-controller/users/{id}/receivedFriendRequests/{senderId}/decline

Get the friend list of a user
GET /api/v1/demo-controller/users/{id}/friendList

Remove a user from the friend list
DELETE /api/v1/demo-controller/users/{id}/friendList/{userToBeDeletedId}

Get all posts by a user
GET /api/v1/demo-controller/users/{id}/posts

Get a list of all posts
GET /api/v1/demo-controller/posts

Create a new post
POST /api/v1/demo-controller/posts

Edit an existing post
PUT /api/v1/demo-controller/posts/{id}

Delete a post
DELETE /api/v1/demo-controller/posts/{id}







