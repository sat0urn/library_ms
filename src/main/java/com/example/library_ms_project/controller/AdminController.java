package com.example.library_ms_project.controller;

import com.example.library_ms_project.entity.Book;
import com.example.library_ms_project.entity.SearchedText;
import com.example.library_ms_project.entity.User;
import com.example.library_ms_project.service.BookService;
import com.example.library_ms_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    public BookService bookService;

    @Autowired
    public UserService userService;

    @GetMapping()
    public String getAllUsersAdminPage(
            @ModelAttribute("book") Book book,
            @ModelAttribute("user") User user,
            Model model) {
        List<User> all_users = userService.getAllUsers();
        List<String> passwords = new ArrayList<>();

        for (User u : all_users) {
            if (u.getRoles().get(0).getName().equals("ROLE_LIBRARIAN")) {
                passwords.add(userService.generateLibrarianPassword(u));
            } else {
                passwords.add("SECRET");
            }
        }

        model.addAttribute("passwords", passwords);
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/books")
    public String showAllBooks(
            @ModelAttribute("book") Book book,
            @ModelAttribute("user") User user,
            @ModelAttribute("new_title") SearchedText new_title,
            Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "admin_books";
    }

    @PostMapping("/books/edit")
    public String editChosenBook(
            @ModelAttribute("book") Book book,
            @ModelAttribute("new_title") SearchedText new_title
    ) {
        Book b = bookService.findByName(book.getName());

        if (b != null) {
            if (new_title.getText().isEmpty()) {
                b.setName(book.getName());
            } else {
                b.setName(new_title.getText());
            }

            if (book.getAuthor().isEmpty()) {
                b.setAuthor(b.getAuthor());
            } else {
                b.setAuthor(book.getAuthor());
            }

            if (book.getYear() == 1900) {
                b.setYear(b.getYear());
            } else {
                b.setYear(book.getYear());
            }

            bookService.updateBook(b);
        }

        return "redirect:/admin/books";
    }

    @PostMapping("/books/add")
    public String addNewBook(@ModelAttribute("book") Book book) {
        bookService.save(book);
        return "redirect:/admin/books";
    }

    @PostMapping("/librarian/generate")
    public String generateLibrarian(
            @ModelAttribute("user") User user
    ) {
        userService.generateLibrarian(user);

        return "redirect:/admin";
    }
}
