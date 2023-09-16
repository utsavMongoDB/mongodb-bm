package com.demo.mongodb.MongoBenchmark.service;

import com.demo.mongodb.MongoBenchmark.model.OrderItems;
import com.demo.mongodb.MongoBenchmark.repo.OrderItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderItemsService {

    @Autowired
    private OrderItemsRepository orderItemsRepository;
    static Random random = new Random();

    public void saveOrderItems(int orderId) throws Exception {
        try {
            int itemCount = 100;
            for (int itemId = 0; itemId < itemCount; itemId++) {
                OrderItems orderItems = generateRandomOrderItems(orderId, itemId);
                orderItemsRepository.save(orderItems);
            }
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private static OrderItems generateRandomOrderItems(int orderId, int itemId) {
        int product_id = generateRandomNumber(1, 10000);
        int quantity = random.nextInt(5) + 1;
        int rate = generateRandomNumber(50, 200);
        int total = quantity * rate;
        int order_item_status = random.nextInt(2);

        int item_id = (orderId - 1) * 100 + itemId + 1;
        System.out.println("item_id : " + item_id);
        OrderItems ordersItemDto = new OrderItems();
//        ordersItemDto.setOrderItemId(item_id);
        ordersItemDto.setId(item_id);
        ordersItemDto.setRate(rate);
        ordersItemDto.setQuantity(quantity);
        ordersItemDto.setTotal(total);
        ordersItemDto.setOrderItemStatus(order_item_status);
        ordersItemDto.setProductId(product_id);
        ordersItemDto.setOrderId(orderId);

        return ordersItemDto;
    }

    private static int generateRandomNumber(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
}
