package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    CreateUserRequest createUserRequest;

    @Test
    public void createUserWithInvalidPassword(){
        // mock
        String password = "hi";
        when(createUserRequest.getPassword()).thenReturn(password);
        when(createUserRequest.getUsername()).thenReturn("username");

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        response.getStatusCode().equals(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createUser(){
        // mock
        String password = "helloWorld";
        String username = "username";
        String encodedPassword = "thisIsEncoded";
        when(createUserRequest.getPassword()).thenReturn(password);
        when(createUserRequest.getPasswordConfirm()).thenReturn(password);
        when(createUserRequest.getUsername()).thenReturn(username);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        ResponseEntity<User> response = userController.createUser(createUserRequest);

        // verify response
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        User user = response.getBody();
        assertEquals(username, user.getUsername());
        assertEquals(encodedPassword, user.getPassword());
        assertNotNull(user.getCart());
        assertNull(user.getCart().getItems());
        assertEquals(user, user.getCart().getUser());
    }

    @Test
    public void findById(){
        // mock repository to test controller
        User createdUser = TestUtils.createOneUser();
        long id = createdUser.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(createdUser));

        // test findById
        ResponseEntity<User> response = userController.findById(id);

        // verify response
        assertTrue(response.getStatusCode().equals(HttpStatus.OK));
        User responseUser = response.getBody();
        assertNotNull(responseUser);
        assertEquals(createdUser.getId(), responseUser.getId());
        assertEquals(createdUser.getUsername(), responseUser.getUsername());
    }

    @Test
    public void findByNotExistUserName(){
        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<User> response = userController.findByUserName("test");
        assertTrue(response.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

    @Test
    public void findByUserName(){
        // mock repository to test controller
        User createdUser = TestUtils.createOneUser();
        String username = createdUser.getUsername();
        when(userRepository.findByUsername(username)).thenReturn(createdUser);

        // test findByUserName
        ResponseEntity<User> response = userController.findByUserName(username);

        // verify
        assertTrue(response.getStatusCode().equals(HttpStatus.OK));
        User responseUser = response.getBody();
        assertNotNull(responseUser);
        assertEquals(createdUser.getId(), responseUser.getId());
        assertEquals(username, responseUser.getUsername());
    }

}
