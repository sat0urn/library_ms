package com.example.library_ms_project.service;

import com.example.library_ms_project.entity.Book;
import com.example.library_ms_project.entity.User;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    void save(Book book);

    Book findBookById(String bookId);

    Book findByName(String bookName);

    void updateBook(Book book);
}
