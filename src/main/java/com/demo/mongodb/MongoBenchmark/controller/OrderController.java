package com.demo.mongodb.MongoBenchmark.controller;

import com.demo.mongodb.MongoBenchmark.model.Orders;
import com.demo.mongodb.MongoBenchmark.model.Product;
import com.demo.mongodb.MongoBenchmark.repo.OrderRepository;
import com.demo.mongodb.MongoBenchmark.repo.OrdersRepository;
import com.demo.mongodb.MongoBenchmark.service.OrderService;
import com.demo.mongodb.MongoBenchmark.utils.DateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/inventory/")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderRepository orderRepository;

    Random random = new Random();

    @PostMapping("/add/{orderId}")
    public Object createProduct(@PathVariable Long orderId) throws Exception {
        return orderService.saveOrder(orderId);
    }


    /**
     * Find all products ordered by a user in a particular date range
     * @param userId
     * @return
     */
    @GetMapping("/get/{userId}")
    public List<Product> findProductsOrderedByUserInDateRange(@PathVariable int userId) {
        try {
            Date randomStartDate = DateGenerator.generateRandomStartDate();
            Date randomEndDate = DateGenerator.generateRandomEndDate(randomStartDate);
//            return ordersRepository.findProductsOrderedByUserInDateRange(userId, randomStartDate, randomEndDate);
            return orderRepository.findProductsOrderedByUserInDateRange(userId, randomStartDate, randomEndDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @GetMapping("/findTopProducts")
    public List<Object> findTopProducts() {
        try {
            Date randomStartDate = DateGenerator.generateRandomStartDate();
            Date randomEndDate = DateGenerator.generateRandomEndDate(randomStartDate);
//            return ordersRepository.findTopProductsInDateRange(randomStartDate, randomEndDate);
            return orderRepository.findTopProductsInDateRange(randomStartDate, randomEndDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/findByShipmentId/{shipmentId}")
    public Orders findByShipmentId(@PathVariable int shipmentId) {
        return orderRepository.findByShipmentId(shipmentId);
    }


    @PutMapping("/updateOrderItemStatus/{orderId}")
    public String updateOrderItemStatus(
            @PathVariable Long orderId
    ) {
        int orderStatus = random.nextInt(100);
//        ordersRepository.updateOrderItemStatus(orderId, orderStatus);
        orderRepository.updateOrderItemStatus(orderId, orderStatus);
        return "Order modified: " + orderId;
    }

}