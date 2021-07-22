package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        List<Item> items = TestUtils.createItemsList();

        when(itemRepository.findAll()).thenReturn(items);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(items.get(0)));
        when(itemRepository.findByName("item2")).thenReturn(Arrays.asList(items.get(1)));
    }

    @Test
    public void get_all_items(){

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());


        List<Item> returnedItems = response.getBody();
        assertNotNull(returnedItems);

        List<Item> items = TestUtils.createItemsList();

        assertArrayEquals(items.toArray(), returnedItems.toArray());
    }

    @Test
    public void get_item_by_id(){

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();
        assertNotNull(item);
        assertEquals("item1", item.getName());
        assertEquals(BigDecimal.valueOf(10), item.getPrice());
        assertEquals("desc1", item.getDescription());

    }

    @Test
    public void get_item_by_invalid_id(){

        ResponseEntity<Item> response = itemController.getItemById(2L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void get_item_by_name(){

        ResponseEntity<List<Item>> response = itemController.getItemsByName("item2");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> returnedItems = response.getBody();
        assertNotNull(returnedItems);

        assertEquals("item2", returnedItems.get(0).getName());
        assertEquals(BigDecimal.valueOf(15), returnedItems.get(0).getPrice());
        assertEquals("desc2", returnedItems.get(0).getDescription());

    }

    @Test
    public void get_item_by_invalid_name(){

        ResponseEntity<List<Item>> response = itemController.getItemsByName("item5");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }


}
