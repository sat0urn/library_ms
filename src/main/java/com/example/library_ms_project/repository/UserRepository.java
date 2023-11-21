package com.example.library_ms_project.repository;


import com.example.library_ms_project.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findUserById(String id);
    User findByEmail(String email);

    List<User> findAll();
}
