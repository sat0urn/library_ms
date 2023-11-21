package com.example.library_ms_project.controller;

import com.example.library_ms_project.entity.Book;
import com.example.library_ms_project.service.BookService;
import com.example.library_ms_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

    @Autowired
    public BookService bookService;

    @Autowired
    public UserService userService;

    @GetMapping("/admin")
    public String getAllUsersAdminPage(@ModelAttribute("book") Book book, Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/admin/books")
    public String showAllBooks(@ModelAttribute("book") Book book, Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "admin_books";
    }

    @PostMapping("/admin/books/add")
    public String addNewBook(@ModelAttribute("book") Book book) {
        bookService.save(book);
        return "redirect:/admin";
    }
}
