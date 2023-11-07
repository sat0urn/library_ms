package com.example.library_ms_project.service;

import com.example.library_ms_project.entity.Book;
import com.example.library_ms_project.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.text.ParseException;
import java.util.List;

public interface UserService extends UserDetailsService {
    void save(User user);

    User findUserByEmail(String name);

    User findUserById(String id);

    List<User> getAllUsers();

    void deleteUser(String id);

    User updateUser(User user);

    Book borrowBook(String id, String bookId) throws ParseException;
}
