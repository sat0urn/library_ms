package com.example.library_ms_project.controller;

import com.example.library_ms_project.entity.Book;
import com.example.library_ms_project.entity.BookRequest;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    public BookService bookService;

    @Autowired
    public UserService userService;

    @GetMapping("/profile")
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

            int todayDays = LocalDate.now().getDayOfMonth();
            int leftDays = bookDate.minusDays(todayDays).getDayOfMonth();

            if (todayDays >= bookDate.getDayOfMonth()) {
                leftDays = 0;


            }

            SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            String return_date = DateFor.format(book.getReturnDate());

            model.addAttribute("user_books", user_books);
            model.addAttribute("book", book);
            model.addAttribute("return_date", return_date);
            model.addAttribute("days", leftDays);
        }

        return "profile";
    }

    @GetMapping("/account/{id}")
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

    @PostMapping("/account/{id}")
    public String updateUser(
            @PathVariable("id") String id,
            @ModelAttribute("user") User user
    ) {
        User u = userService.findUserById(id);

        if (!u.getEmail().equals(user.getEmail()) && userService.findUserByEmail(user.getEmail()) != null) {
            u.setEmail(u.getEmail());
        } else {
            u.setEmail(user.getEmail());
        }

        u.setName(user.getName());
        u.setSurname(user.getSurname());
        u.setPhone(user.getPhone());

        userService.updateUser(u);
        return "redirect:/user/account/" + u.getId();
    }

    @PostMapping("/account/photo/add")
    public String addPhoto(
            @RequestParam("image") MultipartFile image,
            @ModelAttribute("id") String id
    ) throws IOException {
        userService.addPhoto(id, image);
        return "redirect:/user/account/" + id;
    }

    @PostMapping("/account/change_password/{id}")
    public String changePassword(
            @PathVariable("id") String id,
            @ModelAttribute("user") User user
    ) {
        User u = userService.findUserById(id);

        u.setPassword(user.getPassword());

        userService.changePassword(u);
        return "redirect:/user/account/" + u.getId();
    }

    @GetMapping("/account/delete/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return "redirect:/";
    }

    @GetMapping("/book/borrow")
    public String borrowBook(
            @ModelAttribute("id") String id,
            @ModelAttribute("bookId") String bookId
    ) {

        BookRequest bookRequest = userService.findBookRequest(id, bookId);

        if (bookRequest == null) {
            userService.createRequest(id, bookId);
        }

        return "redirect:/user/library/" + id;
    }

    @GetMapping("/library/{id}")
    public String showAllBooks(
            Model model,
            @PathVariable("id") String id
    ) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("books", bookService.getAllBooks());
        return "user_books";
    }

    @GetMapping("/history/{id}")
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

    @PostMapping("/history/{id}")
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
