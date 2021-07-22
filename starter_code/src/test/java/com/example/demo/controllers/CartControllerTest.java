package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(TestUtils.createItem());
        cart.setTotal(BigDecimal.valueOf(10));

        User user = new User();
        user.setId(1L);
        user.setUsername("maleskndrany");
        user.setPassword("pass123");
        user.setCart(cart);

        Item item = TestUtils.createItem();

        when(userRepository.findByUsername("maleskndrany")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
    }

    @Test
    public void add_item_to_cart_by_username(){

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("maleskndrany");
        request.setItemId(1L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
        assertEquals(3, cart.getItems().size());
        assertEquals(TestUtils.createItem(), cart.getItems().get(0));
        assertEquals(BigDecimal.valueOf(30), cart.getTotal());
    }

    @Test
    public void add_item_to_cart_by_invalid_username(){

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("user123");
        request.setItemId(1L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void add_invalid_item_to_cart_by_username(){

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("maleskndrany");
        request.setItemId(2L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_item_from_cart_by_username(){

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("maleskndrany");
        request.setItemId(1L);
        request.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
        assertEquals(0, cart.getItems().size());
        assertEquals(BigDecimal.valueOf(0), cart.getTotal());
    }


}
