package com.example.library_ms_project.service;

import com.example.library_ms_project.entity.Book;
import com.example.library_ms_project.entity.BookRequest;
import com.example.library_ms_project.entity.Role;
import com.example.library_ms_project.entity.User;
import com.example.library_ms_project.repository.BookRequestRepository;
import com.example.library_ms_project.repository.UserRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRequestRepository bookRequestRepository;

    @Override
    public void save(User user) {
        if (user.getEmail().equals("zeyin03@gmail.com") && user.getName().equals("zeiin")) {
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setRoles(List.of(new Role("ROLE_ADMIN")));
            userRepository.insert(user);
        } else {
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setRoles(List.of(new Role("ROLE_USER")));
            userRepository.insert(user);
        }
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserById(String id) {
        return userRepository.findUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepository.findUserById(id);
        userRepository.delete(user);
        System.out.println("User " + user.getEmail() + " was deleted from service");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public void updateUser(User user) {
        mongoTemplate.save(user);
    }

    @Override
    public void changePassword(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRoles(List.of(new Role("ROLE_USER")));

        mongoTemplate.save(user);
    }

    @Override
    public void borrowBook(String id, String bookId, int returnDays) throws ParseException {
        Book book = bookService.findById(bookId);

        if (book != null && book.isAvailable()) {
            LocalDate localDate = LocalDate.now();

            if (returnDays > 14) {
                localDate = localDate.plusDays(14);
            }
            if (returnDays <= 0) {
                localDate = localDate.plusDays(1);
            }

            localDate = localDate.plusDays(returnDays);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date date = sdf.parse(localDate.getDayOfMonth() + "/" + localDate.getMonthValue() + "/" + localDate.getYear());

            book.setReturnDate(date);
            book.setAvailable(false);

            User user = findUserById(id);

            user.getBooks().add(book);

            mongoTemplate.save(user);
            mongoTemplate.save(book);
        }
    }

    @Override
    public void addPhoto(String id, MultipartFile file) throws IOException {
        User user = findUserById(id);

        user.setImage(
                new Binary(BsonBinarySubType.BINARY, file.getBytes())
        );

        mongoTemplate.save(user);
    }

    @Override
    public String generatePassword(User user) {
        String first_name = user.getName().toLowerCase();
        String last_name = user.getSurname().toLowerCase();
        String phone = user.getPhone().toString();

        char[] chars = (first_name + last_name).toCharArray();
        int id = 0;
        for (char ch : chars) {
            id += ch;
        }

        String idStr = id % 1000 / 100 + "" + id % 100 / 10;

        return "libms" + first_name + last_name + phone.substring(phone.length() - 2) + idStr;
    }

    @Override
    public void generateLibrarian(User user) {
        String first_name = user.getName().toLowerCase();
        String last_name = user.getSurname().toLowerCase();
        String phone = user.getPhone().toString();
        String email = first_name
                + "_"
                + last_name
                + phone.substring(phone.length() - 4) + "@libms.com";

        String password = generatePassword(user);

        user.setEmail(email);
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);
        user.setRoles(List.of(new Role("ROLE_LIBRARIAN")));
        userRepository.insert(user);
    }

    @Override
    public BookRequest findBookRequest(String user_id, String book_id) {
        return bookRequestRepository.findBookRequestByUserIdAndBookId(user_id, book_id);
    }

    @Override
    public void createRequest(String user_id, String book_id) {
        mongoTemplate.save(new BookRequest(user_id, book_id, 0));
    }

    @Override
    public void removeRequest(String user_id, String book_id) {
        bookRequestRepository.deleteBookRequestByUserIdAndBookId(user_id, book_id);
    }

    @Override
    public List<BookRequest> getAllBookRequests() {
        return bookRequestRepository.findAll();
    }
}
