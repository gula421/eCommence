package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
    @InjectMocks
    ItemController itemController;

    @Mock
    ItemRepository itemRepository;

    @Test
    public void getItems(){
        // mock item repository
        int numItem = 5;
        List<Item> items = TestUtils.createItems(numItem);
        when(itemRepository.findAll()).thenReturn(items);

        // verify controller method
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(numItem ,response.getBody().size());
    }

    @Test
    public void getItemById(){
        // mock itemRepository
        Item oneItem = TestUtils.createOneItem(1L);
        Long id = oneItem.getId();
        when(itemRepository.findById(id)).thenReturn(Optional.of(oneItem));

        // verify controller
        ResponseEntity<Item> response = itemController.getItemById(id);
        Item responseItem = response.getBody();
        assertNotNull(responseItem);
        assertEquals(id, responseItem.getId());
    }

    @Test
    public void getItemsByName(){
        // mock itemRepository
        int numItem = 3;
        List<Item> items = TestUtils.createItems(numItem);
        String itemName = items.get(0).getName();
        String sharedName = itemName.substring(0, itemName.length()-1);
        when(itemRepository.findByName(sharedName)).thenReturn(items);

        // verify controller
        ResponseEntity<List<Item>> response = itemController.getItemsByName(sharedName);
        List<Item> responseItems = response.getBody();
        assertNotNull(responseItems);
        assertEquals(numItem ,responseItems.size());

    }
}
