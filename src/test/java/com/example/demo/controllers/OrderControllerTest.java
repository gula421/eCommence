package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {
    @InjectMocks
    OrderController orderController;

    @Mock
    UserRepository userRepository;

    @Mock
    OrderRepository orderRepository;


    @Test
    public void submitUserNotExist(){
        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<UserOrder> response = orderController.submit("anyname");
        assertTrue(response.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

    @Test
    public void submit(){
        User user = TestUtils.createOneUser();
        String username = user.getUsername();
        Cart cart = TestUtils.createACartForUser(1L, user);
        when(userRepository.findByUsername(any())).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit(username);
        assertTrue(response.getStatusCode().equals(HttpStatus.OK));
        UserOrder responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(user, responseBody.getUser());
        assertEquals(cart.getItems().size(), responseBody.getItems().size());
        assertEquals(cart.getTotal(), responseBody.getTotal());
    }

    @Test
    public void getOrdersForNotExistUser(){
        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("anyname");
        assertTrue(response.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

    @Test
    public void getOrdersForUser(){
        int numOrder = 4;
        User user = TestUtils.createOneUser();
        List<UserOrder> orders = TestUtils.createUserOrders(numOrder, user);
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(orderRepository.findByUser(any())).thenReturn(orders);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        List<UserOrder> responseBody = response.getBody();
        assertTrue(response.getStatusCode().equals(HttpStatus.OK));
        assertNotNull(responseBody);
        assertEquals(numOrder, responseBody.size());
        assertEquals(user, responseBody.get(0).getUser());
    }

}
