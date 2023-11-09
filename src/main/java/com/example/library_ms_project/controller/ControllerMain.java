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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;


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

    @GetMapping("/user/profile")
    public String profilePage(Model model, Authentication authentication) {

        User user = userService.findUserByEmail(authentication.getName());
        List<Book> library_books = bookService.getAllBooks();
        List<Book> user_books = user.getBooks();

        model.addAttribute("user", user);
        model.addAttribute("library_books", library_books);
        if (user_books == null || user_books.isEmpty()) {
            Date date = new Date();

            Book book = new Book(
                    "1004281",
                    "Info about borrowed book will be there",
                    "Author name",
                    1999,
                    false,
                    date
            );

            SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss", Locale.ENGLISH);
            String return_date = DateFor.format(book.getReturnDate());

            model.addAttribute("user_books", null);
            model.addAttribute("book", book);
            model.addAttribute("return_date", return_date);
            model.addAttribute("days", 5);
        } else {
            Book book = user_books.get(0);
            int maxDays = 14;
            LocalDate localDate = LocalDate.now();
            int today = localDate.getDayOfMonth();
            int leftDays = maxDays - today;

            SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss", Locale.ENGLISH);
            String return_date = DateFor.format(book.getReturnDate());

            model.addAttribute("user_books", user_books);
            model.addAttribute("book", book);
            model.addAttribute("return_date", return_date);
            model.addAttribute("days", leftDays);
        }
        return "profile";
    }

    @GetMapping("/user/account/{id}")
    public String userInformationPage(
            @PathVariable("id") String id,
            Model model
    ) {
        User user = userService.findUserById(id);

        if (user.getBooks() == null || user.getBooks().isEmpty()) {
            model.addAttribute("user_books_number", 0);
        } else {
            model.addAttribute("user_books_number", user.getBooks().size());
        }

        model.addAttribute("user", user);
        if (user.getImage() != null) {
            model.addAttribute("user_image",
                    Base64.getEncoder().encodeToString(user.getImage().getData()));
        }
        return "account";
    }

    @PostMapping("/user/photo/add")
    public String addPhoto(
            @RequestParam("image") MultipartFile image,
            @ModelAttribute("id") String id
    ) throws IOException {
        userService.addPhoto(id, image);
        return "redirect:/user/account/" + id;
    }

    @PostMapping("/user/account/{id}")
    public String updateUser(
            @PathVariable("id") String id,
            @ModelAttribute("user") User user
    ) {
        User u = userService.findUserById(id);

        u.setName(user.getName());
        u.setSurname(user.getSurname());
        u.setPhone(user.getPhone());
        u.setEmail(user.getEmail());

        userService.updateUser(u);
        return "redirect:/user/account/" + u.getId();
    }

    @PostMapping("/user/account/change_password/{id}")
    public String changePassword(
            @PathVariable("id") String id,
            @ModelAttribute("user") User user
    ) {
        User u = userService.findUserById(id);

        u.setPassword(user.getPassword());

        userService.changePassword(u);
        return "redirect:/user/account/" + u.getId();
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return "redirect:/";
    }

    @GetMapping("/user/book/borrow")
    public String borrowBook(
            Model model,
            @ModelAttribute("id") String id,
            @ModelAttribute("bookId") String bookId
    ) throws ParseException {
        Book book = userService.borrowBook(id, bookId);
        if (book != null) {
            User user = userService.findUserById(id);
            model.addAttribute("user", user);
            model.addAttribute("book", book);
            model.addAttribute("days", 14);
            return "redirect:/user/profile";
        }
        return "redirect:/user/library/" + id;
    }

    @GetMapping("/user/library/{id}")
    public String showAllBooks(
            Model model,
            @PathVariable("id") String id
    ) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("book", bookService.getAllBooks());
        return "user_books";
    }

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
