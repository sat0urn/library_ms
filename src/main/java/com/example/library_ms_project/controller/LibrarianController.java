package com.example.library_ms_project.controller;

import com.example.library_ms_project.entity.Book;
import com.example.library_ms_project.entity.BookRequest;
import com.example.library_ms_project.entity.User;
import com.example.library_ms_project.service.BookService;
import com.example.library_ms_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LibrarianController {

    @Autowired
    public BookService bookService;

    @Autowired
    public UserService userService;

    @GetMapping("/librarian")
    public String librarianPage(
            Model model,
            @ModelAttribute("book_request") BookRequest bookRequest
    ) {
        List<BookRequest> bookRequestList = userService.getAllBookRequests();

        List<User> users = new ArrayList<>();
        List<Book> books = new ArrayList<>();

        if (bookRequestList != null && !bookRequestList.isEmpty()) {
            for (BookRequest bookReq : bookRequestList) {
                users.add(userService.findUserById(bookReq.getUserId()));
                books.add(bookService.findById(bookReq.getBookId()));
            }
        }

        model.addAttribute("users", users);
        model.addAttribute("books", books);

        return "librarian";
    }

    @PostMapping("/librarian/reserve/{id}/{bookId}")
    public String reserveBook(
            @PathVariable("id") String user_id,
            @PathVariable("bookId") String book_id,
            @ModelAttribute("book_request") BookRequest bookRequest
    ) throws ParseException {

        userService.borrowBook(user_id, book_id, bookRequest.getReturnDays());

        userService.removeRequest(user_id, book_id);

        return "librarian";
    }

}
