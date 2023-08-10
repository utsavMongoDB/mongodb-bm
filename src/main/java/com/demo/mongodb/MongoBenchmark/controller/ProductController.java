package com.demo.mongodb.MongoBenchmark.controller;


import com.demo.mongodb.MongoBenchmark.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @PostMapping("/add/{productId}")
    public String saveProduct(@PathVariable int productId) {
        return productService.saveProduct(productId);
    }
}
