package com.example.library_ms_project.controller;

import com.example.library_ms_project.entity.Book;
import com.example.library_ms_project.entity.User;
import com.example.library_ms_project.service.BookService;
import com.example.library_ms_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class ControllerMain {
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }


    @GetMapping("/registration")
    public String registerUser(@ModelAttribute("user") User user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String saveUserToDB(@ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage(@ModelAttribute("user") User user) {
        return "login";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, Authentication authentication) {

        User user = userService.findUserByEmail(authentication.getName());
        model.addAttribute("user", user);
        return "profile";

    }

    @GetMapping("/user_info/{id}")
    public String userInformationPage(@PathVariable("id") String id, Model model) {
        User user = userService.findUserById(id);

        model.addAttribute("user", user);
        System.out.println(user.getName());
        return "account";
    }

    @GetMapping("/deleteMe/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return "redirect:/";
    }

    @PostMapping("/user_info/{id}")
    public String updateUser(@PathVariable("id") String id, @ModelAttribute("user") User user) {
        User user1 = userService.updateUser(user);
        System.out.println(user1);

        return "redirect:/user_info/" + user.getId();
    }

    @GetMapping("/admin")
    public String getAllUsersAdminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/admin/books")
    public String showAllBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }

    @GetMapping("/admin/add_new_book")
    public String addNewBookPage(@ModelAttribute("book") Book book) {
        return "addBook";
    }

    @PostMapping("/admin/add_new_book")
    public String addNewBookToDB(@ModelAttribute("book") Book book) {
        bookService.save(book);
        return "redirect:/admin";
    }

}
