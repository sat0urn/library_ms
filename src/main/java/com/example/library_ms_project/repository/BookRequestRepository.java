package com.example.library_ms_project.repository;

import com.example.library_ms_project.entity.Book;
import com.example.library_ms_project.entity.BookRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRequestRepository extends MongoRepository<BookRequest, String> {
    List<BookRequest> findAll();

    void deleteBookRequestByUserIdAndBookId(String user_id, String book_id);

    BookRequest findBookRequestByUserIdAndBookId(String user_id, String book_id);
}
