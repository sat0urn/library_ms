package com.example.otyrar_project.service;

import com.example.otyrar_project.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    void save(User user);

   /* boolean provenAccount(String name, String password);*/

    User findUserByEmail(String name);

    User findUserById(String id);

    List<User> getAllUsers();

    void deleteUser(String id);

    User updateUser(User user);

}
