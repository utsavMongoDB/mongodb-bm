package com.demo.mongodb.MongoBenchmark.service;

import com.demo.mongodb.MongoBenchmark.model.Orders;
import com.demo.mongodb.MongoBenchmark.model.Product;
import com.demo.mongodb.MongoBenchmark.repo.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    public Orders saveOrder(Long orderId) throws Exception {
        try {
            Orders order = generateOrdersDto(orderId);
            return ordersRepository.save(order);
        }
        catch (Exception e){
            throw new Exception("OrderId already exists");
        }
    }


    public static Orders generateOrdersDto(Long orderId) {
        Random random = new Random();

        LocalDate startDate = LocalDate.of(2023, 7, 15);
        LocalDate endDate = LocalDate.of(2023, 7, 31);
        LocalDate orderDate = generateRandomDate(startDate, endDate);

        String clientName = "client_" + orderId;
        String clientContact = "+1-555-" + random.nextInt(1000) + "-" + random.nextInt(10000);
        int subTotal = generateRandomNumber(100, 1000);
        int vat = subTotal / 10;
        int totalAmount = subTotal + vat;
        int discount = generateRandomNumber(0, 50);
        int grandTotal = totalAmount - discount;
        int paid = grandTotal;
        int due = 0;
        int paymentType = random.nextInt(2) + 1;
        int paymentStatus = random.nextInt(2) + 1;
        int paymentPlace = random.nextInt(2) + 1;
        String gstn = generateRandomGSTN();
        int orderStatus = 0;
        int userId = generateRandomNumber(1, 10000);
        List<Map<String, Object>> orderItems = generateRandomOrderItems();

        Orders order = new Orders();
        order.setOrderId(orderId);
        order.setOrderDate(orderDate);
        order.setClientName(clientName);
        order.setClientContact(clientContact);
        order.setSubTotal(subTotal);
        order.setVat(vat);
        order.setTotalAmount(totalAmount);
        order.setDiscount(discount);
        order.setGrandTotal(grandTotal);
        order.setPaid(paid);
        order.setDue(due);
        order.setPaymentType(paymentType);
        order.setPaymentStatus(paymentStatus);
        order.setPaymentPlace(paymentPlace);
        order.setGstn(gstn);
        order.setOrderStatus(orderStatus);
        order.setUserId(userId);
        order.setOrderItem(orderItems);
        order.setDeliveryDetails(generateDeliveryDetails(orderId));
        return order;
    }

    private static final String GSTN_PREFIX = "GSTN";

    private static LocalDate generateRandomDate(LocalDate startDate, LocalDate endDate) {
        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    private static String generateRandomGSTN() {
        int number = generateRandomNumber(1000, 9999);
        return GSTN_PREFIX + number;
    }

    private static List<Map<String, Object>> generateRandomOrderItems() {
        Random random = new Random();
        int itemCount = random.nextInt(3) + 3; // Generate between 3 and 5 items

        List<Map<String, Object>> orderItems = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            Map<String, Object> orderItem = new HashMap<>();
            orderItem.put("order_item_id", i);
            orderItem.put("product_id", generateRandomNumber(1, 10000));
            orderItem.put("quantity", random.nextInt(5) + 1);
            orderItem.put("rate", generateRandomNumber(50, 200));
            orderItem.put("total", (int) orderItem.get("quantity") * (int) orderItem.get("rate"));
            orderItem.put("order_item_status", random.nextInt(2));
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private static Map<String, Object> generateDeliveryDetails(Long orderId) {
        Random random = new Random();
        Map<String, Object> orderItem = new HashMap<>();
        orderItem.put("shipment_id", orderId);
        orderItem.put("log_com_code", generateRandomNumber(1, 100));
        orderItem.put("delivery_status", random.nextInt(2));

        return orderItem;
    }

    private static int generateRandomNumber(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
}
