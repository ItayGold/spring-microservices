package com.in28minutes.rest.webservices.restfulwebservices.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    User user;

    List<Post> userPosts;

    Post post;

    @BeforeEach
    public void setUp(){
        userPosts = new ArrayList<>();
    }

    @Test
    public void createPost(){

        user =  userRepository.findById(10002).get();
        post = new Post(null, "Spring boot is good.", user);

        userPosts.add(post);
        user.setPosts(userPosts);
        Post myPost = postRepository.save(post);
        assertTrue(myPost.getId()!=null);
        assertTrue(myPost.getUser().getName().equals("Jill"));
        User foundUser = userRepository.findById(10002).get();
        List<Post> post =foundUser.getPosts();
        assertTrue( post.get(0).getDescription().equals("Spring boot is good."));

    }

    @Test
    public void deleteUser(){
       user = new User(null, "Alan", new Date());
       post  = new Post(null, "test delete", user);

       userPosts.add(post);
       user.setPosts(userPosts);
       User userCreated = userRepository.save(user);
       int userID = userCreated.getId();
       Post userPost = postRepository.save(post);
       int userPostID = userPost.getId();
       assertTrue(userPost.getId()!= null);
       Optional<User> userOptional = userRepository.findById(userID);
       assertTrue(userOptional.isPresent());
       List<Post> postsByUser = userOptional.get().getPosts();
       assertTrue(postsByUser.get(0).getDescription().equals("test delete"));

       userRepository.deleteById(userID);
       Optional<Post> relatedPost = postRepository.findById(userPostID);
       assertTrue(relatedPost.isEmpty());

    }

   @AfterEach
    public void clean(){
         userPosts.clear();
   }
}