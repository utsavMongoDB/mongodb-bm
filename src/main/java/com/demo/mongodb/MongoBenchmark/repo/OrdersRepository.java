package com.demo.mongodb.MongoBenchmark.repo;

import com.demo.mongodb.MongoBenchmark.model.Orders;
import com.demo.mongodb.MongoBenchmark.model.Product;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@EnableMongoAuditing
public interface OrdersRepository extends MongoRepository<Orders, String> {

    @Query("{'orderId': ?0}")
    List<Orders> findByOrderId(Long orderId);

    @Query(value = "{ 'orderId': :orderId }", fields = "{ 'subTotal': 1 }")
    List<Orders> findSubTotalByOrderId(@Param("orderId") Integer orderId);

//    @Aggregation(pipeline = {
//            "{$match: { 'orderDate': { $gte: :#{#startDate}, $lte: :#{#endDate} } } }",
//            "{$group: { _id: '$userId', total_order_amount: { $sum: '$totalAmount' } } }",
//            "{$sort: { total_order_amount: -1 } }"
//    })
//    List<Map<Integer, Integer>> getTotalOrderAmountInRange(@Param("startDate") Date startDate,
//                                                           @Param("endDate") Date endDate);

    @Aggregation(pipeline = {
            "{$match: { 'orderDate': { $gte: :#{#startDate}, $lte: :#{#endDate} } } }",
            "{$unwind: '$orderItem'}",
            "{$group: { _id: '$orderItem.product_id', order_count: { $sum: 1 } } }",
            "{$sort: { order_count: -1 } }",
            "{$limit: 5}",
            "{$project: {order_id : $_id, order_count: $order_count} }"
    })
    List<Object> findTopProductsInDateRange(@Param("startDate") Date startDate,
                                            @Param("endDate") Date endDate);


    @Aggregation(pipeline = {
            "{$match: { 'userId': :#{#userId}, 'orderDate': { $gte: :#{#startDate}, $lte: :#{#endDate} } } }",
            "{$unwind: '$orderItem'}",
            "{$group: { _id: null, ordered_product_ids: { $addToSet: { $toInt: '$orderItem.product_id' } } } }",
            "{$lookup: { from: 'products', localField: 'ordered_product_ids', foreignField: 'product_id', as: 'ordered_products' } }",
            "{$unwind: '$ordered_products'}",
            "{$replaceRoot: { newRoot: '$ordered_products' } }"
    })
    List<Product> findProductsOrderedByUserInDateRange(@Param("userId") int userId,
                                                       @Param("startDate") Date startDate,
                                                       @Param("endDate") Date endDate);


    @Query(value = "{ 'delivery_details.shipment_id': :#{#shipmentId} }")
    Orders findByShipmentId(int shipmentId);


    @Transactional
    @Query("{ 'orderId': :#{#orderId} }")
    @Update("{'$set': {'orderItem.$[].order_item_status': :#{#newOrderItemStatus} }}")
    void updateOrderItemStatus(@Param("orderId") Long orderId,
                                 @Param("newOrderItemStatus") int newOrderItemStatus);
}