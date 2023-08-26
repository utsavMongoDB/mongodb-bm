package com.demo.mongodb.MongoBenchmark.service;

import com.demo.mongodb.MongoBenchmark.model.User;
import com.demo.mongodb.MongoBenchmark.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String saveUsers(Long userId) {
        User user = generateUserDto(userId);
//        System.out.println(userId);
        userRepository.save(user);
        return "Added User Successfully";
    }

    private static final String[] EMAIL_DOMAINS = {"example.com", "test.com", "domain.com", "user.com"};

    public User generateUserDto(Long userId) {
        String username = generateUsername(userId);
        String password = generatePassword();
        String email = username + "@" + EMAIL_DOMAINS[new Random().nextInt(EMAIL_DOMAINS.length)];

        User user = new User();
        user.setUser_id(userId);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        return user;
    }

    private static String generateUsername(Long userId) {
        return "user" + userId;
    }

    private static String generatePassword() {
        return "Pass" + new Random().nextInt(100000);
    }

}
