package com.example.library_ms_project.repository;


import com.example.library_ms_project.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book,String> {
    List<Book> findAll();
    Book findBookById(String id);
    Book findBookByName(String bookName);
}
