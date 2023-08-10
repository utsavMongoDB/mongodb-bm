package com.demo.mongodb.MongoBenchmark.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "products")
public class Product {

    @JsonProperty("product_id")
    private Long product_id;

    @JsonProperty("product_name")
    private String product_name;

    @JsonProperty("product_image")
    private String product_image;

    @JsonProperty("brand_id")
    private Integer brand_id;

    @JsonProperty("categories_id")
    private Integer categories_id;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("rate")
    private Integer rate;

    @JsonProperty("active")
    private Integer active = 0;

    @JsonProperty("status")
    private Integer status = 0;

}
