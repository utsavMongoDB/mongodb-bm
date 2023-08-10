package com.demo.mongodb.MongoBenchmark.controller;

import com.demo.mongodb.MongoBenchmark.model.Orders;
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
    Random random = new Random();

    @PostMapping("/add/{orderId}")
    public Object createProduct(@PathVariable Long orderId) {
        return orderService.saveOrder(orderId);
    }

    /**
     * Returns all orders in the Database
     *
     * @return
     */
    @GetMapping("/all")
    public List<Orders> findAllOrders() {
        return ordersRepository.findAll();
    }

    /**
     * Find all products ordered by a user in a particular date range
     * @param userId
     * @return
     */
    @GetMapping("/get/{userId}")
    public List<Object[]> findProductsOrderedByUserInDateRange(@PathVariable int userId) {
        try {
            Date randomStartDate = DateGenerator.generateRandomStartDate();
            Date randomEndDate = DateGenerator.generateRandomEndDate(randomStartDate);
            return ordersRepository.findProductsOrderedByUserInDateRange(userId, randomStartDate, randomEndDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns list of all users in the database with the total order amount ordered,
     * ORDERED by order amount
     *
     * @return
     */
    @GetMapping("/getOrderAmount")
    public List<Object[]> getTotalOrderAmountInRange() {
        try {
            Date randomStartDate = DateGenerator.generateRandomStartDate();
            Date randomEndDate = DateGenerator.generateRandomEndDate(randomStartDate);
            return ordersRepository.getTotalOrderAmountInRange(randomStartDate, randomEndDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/findTopProducts")
    public List<Object[]> findTopProducts() {
        try {
            Date randomStartDate = DateGenerator.generateRandomStartDate();
            Date randomEndDate = DateGenerator.generateRandomEndDate(randomStartDate);
//            System.out.println("Start Date :" + randomStartDate);
//            System.out.println("End Date :" + randomEndDate);
            return ordersRepository.findTopProductsInDateRange(randomStartDate, randomEndDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @PutMapping("/updateOrderItemStatus/{orderId}")
    public String updateOrderItemStatus(
            @PathVariable Long orderId
    ) {
        int orderStatus = random.nextInt(100);
        System.out.println(orderStatus);
//        orderService.updateFirstOrderItemStatus(orderId, orderStatus);
        ordersRepository.updateOrderItemStatus(orderId, orderStatus);
        return "Updated: " + orderId;
    }
}