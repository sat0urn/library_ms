package com.example.otyrar_project.service;

import com.example.otyrar_project.entity.User;
import com.example.otyrar_project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Test
    public  void checkMethodOfUserRepository()
    {
        String id = "63f376e7f9980f60b222227e";
        User user = userService.findUserById(id);
        System.out.println(user.getName());
    }

}