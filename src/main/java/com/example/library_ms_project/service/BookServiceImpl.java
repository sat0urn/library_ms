package com.example.library_ms_project.service;

import com.example.library_ms_project.entity.Book;
import com.example.library_ms_project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public void save(Book book) {
        book.setAvailable(true);
        bookRepository.insert(book);
    }

    @Override
    public Book findBookById(String bookId) {
        return bookRepository.findBookById(bookId);
    }

    @Override
    public Book findByName(String bookName) {
        return bookRepository.findBookByName(bookName);
    }

    @Override
    public void updateBook(Book book) {
        bookRepository.save(book);
    }
}
