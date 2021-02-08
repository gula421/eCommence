package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
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

    @Test
    public void createUser(){
        CreateUserRequest request = TestUtils.createOneUserRequest();

        // handel request
        ResponseEntity<User> response = userController.createUser(request);

        // verify response
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        User user = response.getBody();
        assertEquals(user.getUsername(), request.getUsername());
        assertNotNull(user.getId());
    }

    @Test
    public void findById(){
        // mock
        long id = 1L;
        User createdUser = TestUtils.createOneUser();
        when(userRepository.findById(id)).thenReturn(Optional.of(createdUser));

        // test findById
        ResponseEntity<User> response = userController.findById(id);

        // verify response
        User responseUser = response.getBody();
        assertNotNull(responseUser);
        assertEquals(createdUser.getId(), responseUser.getId());
        assertEquals(createdUser.getUsername(), responseUser.getUsername());
        assertNull(responseUser.getCart());

    }

    @Test
    public void findByUserName(){
        // mock
        User createdUser = TestUtils.createOneUser();
        String username = createdUser.getUsername();
        when(userRepository.findByUsername(username)).thenReturn(createdUser);

        // test findByUserName
        ResponseEntity<User> response = userController.findByUserName(username);

        // verify
        User responseUser = response.getBody();
        assertNotNull(responseUser);
        assertEquals(responseUser.getId(), createdUser.getId());
        assertEquals(responseUser.getUsername(), username);

    }
}
