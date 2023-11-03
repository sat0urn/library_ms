package com.example.otyrar_project.repository;


import com.example.otyrar_project.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
   public User findUserById(String id);
   public User findByEmail(String email);
   public List<User> findAll();


}
