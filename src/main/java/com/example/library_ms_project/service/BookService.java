package com.example.library_ms_project.service;

import com.example.library_ms_project.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    void save(Book book);
}
