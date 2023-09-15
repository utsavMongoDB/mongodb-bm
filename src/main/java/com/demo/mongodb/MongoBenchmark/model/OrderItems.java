package com.demo.mongodb.MongoBenchmark.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "order_items")
public class OrderItems {
    @JsonProperty("order_item_id")
    private int orderItemId;

    @JsonProperty("total")
    private int total;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("order_item_status")
    private int orderItemStatus;

    @JsonProperty("rate")
    private int rate;

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("order_id")
    private int orderId;
}

