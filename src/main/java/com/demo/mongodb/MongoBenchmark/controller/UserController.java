package com.demo.mongodb.MongoBenchmark.controller;


import com.demo.mongodb.MongoBenchmark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add/{user_id}")
    public String saveUser(@PathVariable Long user_id) {
        return userService.saveUsers(user_id);
    }
}