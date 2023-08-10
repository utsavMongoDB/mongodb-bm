package com.demo.mongodb.MongoBenchmark.repo;

import com.demo.mongodb.MongoBenchmark.model.Orders;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface OrdersRepository extends MongoRepository<Orders, String> {

    @Query("{'orderId': ?0}")
    List<Order> findByOrderId(Long orderId);

    @Query(value = "{ 'orderId': :orderId }", fields = "{ 'subTotal': 1 }")
    List<Order> findSubTotalByOrderId(@Param("orderId") Integer orderId);

    @Aggregation(pipeline = {
            "{$match: { 'orderDate': { $gte: :#{#startDate}, $lte: :#{#endDate} } } }",
            "{$group: { _id: '$userId', total_order_amount: { $sum: '$totalAmount' } } }",
            "{$sort: { total_order_amount: -1 } }"
    })
    List<Object[]> getTotalOrderAmountInRange(@Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate);

    @Aggregation(pipeline = {
            "{$match: { 'orderDate': { $gte: :#{#startDate}, $lte: :#{#endDate} } } }",
            "{$unwind: '$orderItem'}",
            "{$group: { _id: '$orderItem.product_id', order_count: { $sum: 1 } } }",
            "{$sort: { order_count: -1 } }",
            "{$limit: 5}"
    })
    List<Object[]> findTopProductsInDateRange(@Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate);


    @Aggregation(pipeline = {
            "{$match: { 'userId': :#{#userId}, 'orderDate': { $gte: :#{#startDate}, $lte: :#{#endDate} } } }",
            "{$unwind: '$orderItem'}",
            "{$group: { _id: '$orderItem.product_id' } }"
    })
    List<Object[]> findProductsOrderedByUserInDateRange(@Param("userId") int userId,
                                                       @Param("startDate") Date startDate,
                                                       @Param("endDate") Date endDate);

    @Transactional
    @Query("{ 'orderId': :#{#orderId} }")
    @Update("{'$set': {'orderItem.$[].order_item_status': :#{#newOrderItemStatus} }}")
    int updateOrderItemStatus(@Param("orderId") Long orderId,
                                 @Param("newOrderItemStatus") int newOrderItemStatus);
}