package com.example.library_ms_project.controller;

import com.example.library_ms_project.entity.Book;
import com.example.library_ms_project.entity.SearchedText;
import com.example.library_ms_project.entity.User;
import com.example.library_ms_project.service.BookService;
import com.example.library_ms_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Controller
public class UserController {

    @Autowired
    public BookService bookService;

    @Autowired
    public UserService userService;

    @GetMapping("/user/profile")
    public String profilePage(Model model, Authentication authentication) {

        User user = userService.findUserByEmail(authentication.getName());
        List<Book> library_books = bookService.getAllBooks();
        List<Book> user_books = user.getBooks();

        model.addAttribute("user", user);
        model.addAttribute("library_books", library_books);
        if (user_books.isEmpty()) {
            Date date = new Date();

            Book book = new Book(
                    "1004281",
                    "Default book name",
                    "Default author",
                    1999,
                    false,
                    date
            );

            SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            String return_date = DateFor.format(book.getReturnDate());

            model.addAttribute("user_books", user_books);
            model.addAttribute("book", book);
            model.addAttribute("return_date", return_date);
            model.addAttribute("days", 5);
        } else {
            Book book = user_books.get(user_books.size() - 1);

            LocalDate bookDate = book.getReturnDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            int today = LocalDate.now().getDayOfMonth();
            int leftDays = bookDate.minusDays(today).getDayOfMonth();

            SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
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

        model.addAttribute("user", user);
        if (user.getImage() != null) {
            model.addAttribute("user_image",
                    Base64.getEncoder().encodeToString(user.getImage().getData()));
        }
        return "account";
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

    @PostMapping("/user/account/photo/add")
    public String addPhoto(
            @RequestParam("image") MultipartFile image,
            @ModelAttribute("id") String id
    ) throws IOException {
        userService.addPhoto(id, image);
        return "redirect:/user/account/" + id;
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

    @GetMapping("/user/account/delete/{id}")
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

    @GetMapping("/user/history/{id}")
    public String showHistoryPage(
            Model model,
            @PathVariable("id") String id
    ) {
        User user = userService.findUserById(id);

        List<Book> searched_books = (List<Book>) model.getAttribute("searched_books");
        List<Book> history_books;

        SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        List<String> return_dates = new ArrayList<>();

        if (searched_books != null) {
            history_books = searched_books;
        } else {
            history_books = user.getBooks();
        }

        for (Book book : history_books) {
            return_dates.add(DateFor.format(book.getReturnDate()));
        }

        List<String> return_dates_progress = new ArrayList<>();
        for (Book book : history_books) {
            LocalDate bookDate = book.getReturnDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            int today = LocalDate.now().getDayOfMonth();
            int progress = bookDate.minusDays(today).getDayOfMonth();
            int progress_percent = progress * 100 / 14;

            return_dates_progress.add(progress_percent + "%");
        }

        model.addAttribute("user", user);
        model.addAttribute("books", history_books);
        model.addAttribute("search_books", new SearchedText());
        model.addAttribute("return_dates", return_dates);
        model.addAttribute("return_dates_progress", return_dates_progress);
        return "history";
    }

    @PostMapping("/user/history/{id}")
    public String searchBooksHistory(
            final RedirectAttributes redirectAttributes,
            @PathVariable("id") String id,
            @ModelAttribute("search_books") SearchedText searchedText
    ) {
        User user = userService.findUserById(id);

        List<Book> searchedBooks = new ArrayList<>();

        if (searchedText.getText() != null) {
            for (Book book : user.getBooks()) {
                String user_book = book.getName().toLowerCase();
                String searched_text = searchedText.getText().toLowerCase();
                if (user_book.contains(searched_text)) {
                    searchedBooks.add(book);
                }
            }

            redirectAttributes.addFlashAttribute("searched_books", searchedBooks);
        }

        return "redirect:/user/history/" + id;
    }
}
