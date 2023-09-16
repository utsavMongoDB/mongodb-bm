package com.demo.mongodb.MongoBenchmark.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public OrderItemRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        MappingMongoConverter converter = (MappingMongoConverter) this.mongoTemplate.getConverter();
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }
}