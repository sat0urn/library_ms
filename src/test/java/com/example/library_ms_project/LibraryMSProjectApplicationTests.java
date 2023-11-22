package com.example.library_ms_project;

import com.example.library_ms_project.entity.Book;
import com.example.library_ms_project.entity.User;
import com.example.library_ms_project.service.BookService;
import com.example.library_ms_project.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class LibraryMSProjectApplicationTests {
    @Autowired
    private UserService userService;
    @Autowired
    BookService bookService;

    @Test
    public void checkAdminIS() {
        User user = userService.findUserByEmail("zeyin03@gmail.com");
        Assertions.assertEquals(user.getName(), "zeiin");
    }

    @Test
    public void checkGetBooks() {
        List<Book> books = bookService.getAllBooks();
        Assertions.assertNotEquals(books.size(), 0);
    }

    @Test
    public void testFindUserById() {
        User user = userService.findUserById("655e5013d2089801cc16e03e");
        Assertions.assertEquals(user.getName(), "zeiin");
    }
}
