package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(TestUtils.createItem());
        cart.setTotal(BigDecimal.valueOf(10));

        User user = new User();
        user.setId(1L);
        user.setUsername("maleskndrany");
        user.setPassword("pass123");
        user.setCart(cart);

        UserOrder order = UserOrder.createFromCart(user.getCart());

        when(userRepository.findByUsername("maleskndrany")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(order));
    }

    @Test
    public void submit_order_by_username(){
        ResponseEntity<UserOrder> response = orderController.submit("maleskndrany");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(1, userOrder.getItems().size());
        assertEquals(TestUtils.createItem(), userOrder.getItems().get(0));
        assertEquals(BigDecimal.valueOf(10), userOrder.getTotal());
    }

    @Test
    public void submit_order_by_invalid_username(){
        ResponseEntity<UserOrder> response = orderController.submit("user123");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_orders_by_username(){
        ResponseEntity<UserOrder> response = orderController.submit("maleskndrany");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        ResponseEntity<List<UserOrder>> getResponse = orderController.getOrdersForUser("maleskndrany");
        assertNotNull(getResponse);
        assertEquals(200, getResponse.getStatusCodeValue());

        List<UserOrder> userOrders = getResponse.getBody();
        assertNotNull(userOrders);
        assertEquals(1, userOrders.size());
        assertEquals(TestUtils.createItem(), userOrders.get(0).getItems().get(0));
        assertEquals(BigDecimal.valueOf(10), userOrders.get(0).getTotal());
    }

    @Test
    public void get_orders_by_invalid_username(){
        ResponseEntity<List<UserOrder>> getResponse = orderController.getOrdersForUser("user123");
        assertNotNull(getResponse);
        assertEquals(404, getResponse.getStatusCodeValue());
    }
}
