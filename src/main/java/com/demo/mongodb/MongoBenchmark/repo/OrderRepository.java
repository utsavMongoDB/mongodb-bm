package com.demo.mongodb.MongoBenchmark.repo;

import com.demo.mongodb.MongoBenchmark.model.OrderItems;
import com.demo.mongodb.MongoBenchmark.model.Orders;
import com.demo.mongodb.MongoBenchmark.model.Product;
import com.mongodb.client.model.Aggregates;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.json.JSONArray;
import org.json.JSONObject;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Filters.eq;

import javax.swing.text.Document;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public OrderRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        MappingMongoConverter converter = (MappingMongoConverter) this.mongoTemplate.getConverter();
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }

    public Object findProductsOrderedByUserInDateRange(int userId, Date startDate, Date endDate) throws JSONException {
        AggregationOperation match = Aggregation.match(
                Criteria.where("userId").is(userId)
                        .and("orderDate").gte(startDate).lte(endDate)
        );

        AggregationOperation unwindOrderItem = Aggregation.unwind("orderItem");

        AggregationOperation groupOrderedProductIds = Aggregation.group()
                .addToSet("orderItem.product_id").as("ordered_product_ids");

        LookupOperation lookupProducts = LookupOperation.newLookup()
                .from("products")
                .localField("ordered_product_ids")
                .foreignField("product_id")
                .as("ordered_products");

        AggregationOperation unwindOrderedProducts = Aggregation.unwind("ordered_products");

        ReplaceRootOperation replaceRoot = ReplaceRootOperation.builder().withValueOf("ordered_products");

        Aggregation aggregation = Aggregation.newAggregation(
                match,
                unwindOrderItem,
                groupOrderedProductIds,
                lookupProducts
        );

        AggregationResults<Product> results = mongoTemplate.aggregate(aggregation, "orders", Product.class);
//        JSONObject rootObject = new JSONObject(results.getRawResults().toJson());
//        JSONArray resultsArray = rootObject.getJSONArray("results");
//        if (resultsArray.length() > 0) {
//            JSONObject resultObject = resultsArray.getJSONObject(0);
//            JSONArray orderedProductsArray = resultObject.getJSONArray("ordered_products");
//            return orderedProductsArray.toString();
//        }
        return results.getRawResults();
    }



    public List<Object> findTopProductsInDateRange(Date startDate, Date endDate) {
        AggregationOperation match = Aggregation.match(
                Criteria.where("orderDate").gte(startDate).lte(endDate)
        );

        AggregationOperation unwindOrderItem = Aggregation.unwind("$orderItem");

        AggregationOperation groupByProductId = Aggregation.group("$orderItem.product_id")
                .count().as("order_count");

        AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "order_count");

        AggregationOperation limit = Aggregation.limit(5);

        AggregationOperation project = Aggregation.project("_id");

        Aggregation aggregation = Aggregation.newAggregation(
                match,
                unwindOrderItem,
                groupByProductId,
                sort,
                limit,
                project
        );

        return mongoTemplate.aggregate(aggregation, "orders", Object.class).getMappedResults();
    }

    public void updateOrderItemStatus(Long orderId, int newOrderItemStatus) {
        Query query = new Query(Criteria.where("orderId").is(orderId));
        Update update = new Update().set("orderItem.$[].order_item_status", newOrderItemStatus);

        mongoTemplate.updateFirst(query, update, "orders");
    }

    public Orders findByShipmentId(int shipmentId) {
        Criteria criteria = Criteria.where("deliveryDetails.shipment_id").is(shipmentId);
        Query query = new Query(criteria);

        return mongoTemplate.findOne(query, Orders.class);
    }


    public Object findOrdersByShipmentId_v2(int clientName) {
        // Create the aggregation operations
//        AggregationOperation match = Aggregation.match(Criteria.where("deliveryDetails.shipment_id").is(clientName));
//        AggregationOperation lookup = Aggregation.lookup("order_items", "orderId", "orderId", "result");
//        AggregationOperation projection = Aggregation.project("result").andExclude("_id");

        // Build the aggregation pipeline
//        Aggregation aggregation = Aggregation.newAggregation(match, lookup, projection);

        // Execute the aggregation query
        Bson match = match(eq("deliveryDetails.shipment_id", clientName));
        Bson lookup = Aggregates.lookup("order_items", "orderId", "orderId", "result");
        Bson project = project(fields(excludeId(), include("result")));

        Collection<org.bson.Document> results = mongoTemplate.getCollection("orders").aggregate(Arrays.asList(match, lookup, project))
                .into(new ArrayList<>());

        return results;
//        return Collections.singletonList(mongoTemplate.aggregate(aggregation, "orders", OrderItems.class)
//                .getRawResults().get("results")).get(0);
    }
}
