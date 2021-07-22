package com.example.demo;

import com.example.demo.model.persistence.Item;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject){

        boolean wasPrivate = false;

        try {

            Field field = target.getClass().getDeclaredField(fieldName);

            if(!field.isAccessible()){
                field.setAccessible(true);
                wasPrivate = true;
            }

            field.set(target, toInject);

            if(wasPrivate){
                field.setAccessible(false);
            }

        }catch (NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }
    }

    public static Item createItem(){

        Item item = new Item();
        item.setId(1L);
        item.setName("item1");
        item.setPrice(BigDecimal.valueOf(10));
        item.setDescription("desc1");

        return item;
    }

    public static List<Item> createItemsList(){
        List<Item> items = new ArrayList<>();

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item1");
        item1.setPrice(BigDecimal.valueOf(10));
        item1.setDescription("desc1");

        items.add(item1);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item2");
        item2.setPrice(BigDecimal.valueOf(15));
        item2.setDescription("desc2");

        items.add(item2);

        Item item3 = new Item();
        item3.setId(3L);
        item3.setName("item3");
        item3.setPrice(BigDecimal.valueOf(20));
        item3.setDescription("desc3");

        items.add(item3);

        return items;
    }
}
