package com.demo.mongodb.MongoBenchmark.service;

import com.demo.mongodb.MongoBenchmark.model.Product;
import com.demo.mongodb.MongoBenchmark.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    public String saveProduct(int productId) {
        Product product = generateProductDto(productId);
        productRepo.save(product);
        return "Added Product Successfully";
    }


    public static Product generateProductDto(int productId) {
        Product product = new Product();
        product.setProduct_id((long) productId);
        product.setProduct_name("Product " + productId);
        product.setProduct_image("image" + productId + ".jpg");
        product.setBrand_id(generateRandomNumber(1, 10));
        product.setCategories_id(generateRandomNumber(1, 5));
        product.setQuantity(generateRandomNumber(1, 100));
        product.setRate(generateRandomNumber(10, 1000) );
        product.setActive(1);
        product.setStatus(0);

        return product;
    }

    private static int generateRandomNumber(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

}