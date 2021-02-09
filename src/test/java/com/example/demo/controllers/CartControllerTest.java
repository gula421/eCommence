package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {
    @InjectMocks
    CartController cartController;

    @Mock
    UserRepository userRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    ModifyCartRequest modifyCartRequest;

    @Test
    public void addTocartUserNotExist(){
        // mock userRepository
        when(userRepository.findByUsername(any())).thenReturn(null);
        // verify  CartController
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertTrue(response.getStatusCode().equals(HttpStatus.NOT_FOUND));
        assertNull(response.getBody());
    }

    @Test
    public void addTocartItemNotExist(){
        // verify CartController
        ResponseEntity<Cart> repsonse = cartController.addTocart(modifyCartRequest);
        assertEquals(repsonse.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void addTocart() {
        Long cartId = 1L;
        User user = TestUtils.createOneUser();
        String username = user.getUsername();
        Cart cart = TestUtils.createACartForUser(cartId, user);
        Item item = TestUtils.createOneItem(1L);
        int itemQuantity = 5;
        BigDecimal expectedTotal = cart.getTotal().add(item.getPrice().multiply(BigDecimal.valueOf(itemQuantity)));
        int expectedQuantity = cart.getItems().size()+itemQuantity;

        // mock user, item repository and modifyCartRequest
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        when(modifyCartRequest.getQuantity()).thenReturn(itemQuantity);
        when(modifyCartRequest.getUsername()).thenReturn(username);

        // verify cartController
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertTrue(response.getStatusCode().equals(HttpStatus.OK));
        Cart responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(cartId , responseBody.getId());
        assertEquals(expectedQuantity,responseBody.getItems().size());
        assertEquals(expectedTotal , responseBody.getTotal());
        assertEquals(user, responseBody.getUser());


    }

    @Test
    public void removeFromcartUserNotExist() {
        // mock userRepository
        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertTrue(response.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

    @Test
    public void removeFromcartItemNotExist(){
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertTrue(response.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

    @Test
    public void removeFromCart(){
        Long cartId = 1L;
        User user = TestUtils.createOneUser();
        String username = user.getUsername();
        Cart cart = TestUtils.createACartForUser(cartId, user);
        Item itemToRemove = TestUtils.createOneItem(1L);
        BigDecimal expectedTotal = cart.getTotal().subtract(itemToRemove.getPrice());
        int expectedQuantity = cart.getItems().size()-1;

        // mock
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(itemRepository.findById(any())).thenReturn(Optional.of(itemToRemove));
        when(modifyCartRequest.getQuantity()).thenReturn(1);

        // verify cartController
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        Cart responseBody = response.getBody();
        assertTrue(response.getStatusCode().equals(HttpStatus.OK));
        assertNotNull(responseBody);
        assertEquals(expectedQuantity, responseBody.getItems().size());
        assertEquals(expectedTotal, responseBody.getTotal());
        assertEquals(user, responseBody.getUser());
    }
}