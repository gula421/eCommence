package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestUtils {
    /**
     * helper method for UserControllerTest.findById and findByUserName
     * */
    public static User createOneUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("oneUserName");
        user.setPassword("oneUserPassword");
        return user;
    }

    /**
     * Hepler methods for ItemControllerTest.getItems and getItemsByName
     * */
    public static List<Item> createItems(int numItems){
        List<Item> list = new ArrayList<>();

        for (int i=0; i<numItems; i++){
            Item item = new Item();
            item.setId((long)i+1);
            item.setName("name"+i);
            item.setDescription("description for item "+i);
            item.setPrice(BigDecimal.valueOf(new Random().nextDouble()));
            list.add(item);
        }
        return list;
    }

    /**
     * Hepler method for ItemControllerTest.getItemById
     * */
    public static Item createOneItem(long id){
        Item item = new Item();
        item.setName("oneName");
        item.setDescription("oneDescription");
        item.setPrice(BigDecimal.valueOf(new Random().nextDouble()));
        item.setId(id);
        return item;
    }

    /**
     * helper method for create a cart
     * */
    public static Cart createACartForUser(Long cartId, User user){
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setUser(user);
        // randomly put in some items
        List<Item> items = createItems(4);
        items.forEach(item -> {cart.addItem(item);});
        // set the cart to the user
        user.setCart(cart);
        return cart;
    }

    /**
     * Helper method of orderController.getOrdersForUser
     * */
    public static List<UserOrder> createUserOrders(int numOrders, User user){
        List<UserOrder> list = new ArrayList<>();
        for(int i=0; i<numOrders; i++){
            UserOrder order = new UserOrder();
            order.setUser(user);
            order.setItems(createItems(3));
            order.setTotal(BigDecimal.valueOf(new Random().nextDouble()));
            order.setId((long)i);
            list.add(order);
        }
        return list;
    }
}
