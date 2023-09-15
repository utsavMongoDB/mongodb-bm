package com.demo.mongodb.MongoBenchmark.repo;

import com.demo.mongodb.MongoBenchmark.model.OrderItems;
import com.demo.mongodb.MongoBenchmark.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderItemsRepository extends MongoRepository<OrderItems, String> {
}
