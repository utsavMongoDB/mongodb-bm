package com.demo.mongodb.MongoBenchmark.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "orders")
public class Orders {

    @JsonProperty("orderId")
    private Long orderId;

    @JsonProperty("orderDate")
    private LocalDate orderDate;

    @JsonProperty("clientName")
    private String clientName;

    @JsonProperty("clientContact")
    private String clientContact;

    @JsonProperty("subTotal")
    private int subTotal;

    @JsonProperty("vat")
    private int vat;

    @JsonProperty("totalAmount")
    private int totalAmount;

    @JsonProperty("discount")
    private int discount;

    @JsonProperty("grandTotal")
    private int grandTotal;

    @JsonProperty("paid")
    private int paid;

    @JsonProperty("due")
    private int due;

    @JsonProperty("paymentType")
    private int paymentType;

    @JsonProperty("paymentStatus")
    private int paymentStatus;

    @JsonProperty("paymentPlace")
    private int paymentPlace;

    @JsonProperty("gstn")
    private String gstn;

    @JsonProperty("orderStatus")
    private int orderStatus = 0;

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("orderItem")
    private List<Map<String, Object>> orderItem;

    @JsonProperty("deliveryDetails")
    private Map<String, Object> deliveryDetails;
}

