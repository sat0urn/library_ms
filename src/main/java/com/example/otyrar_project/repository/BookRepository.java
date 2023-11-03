package com.example.otyrar_project.repository;


import com.example.otyrar_project.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book,String> {
    public List<Book> findAll();
}
