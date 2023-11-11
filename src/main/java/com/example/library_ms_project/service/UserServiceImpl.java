package com.example.library_ms_project.service;

import com.example.library_ms_project.entity.Book;
import com.example.library_ms_project.entity.Role;
import com.example.library_ms_project.entity.User;
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
    MongoTemplate mongoTemplate;
    @Autowired
    private BookService bookService;

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
    public User updateUser(User user) {
        return mongoTemplate.save(user);
    }

    @Override
    public void changePassword(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRoles(List.of(new Role("ROLE_USER")));

        mongoTemplate.save(user);
    }

    @Override
    public Book borrowBook(String id, String bookId) throws ParseException {
        Book book = bookService.findById(bookId);
        if (book != null && book.isAvailable()) {
            LocalDate localDate = LocalDate.now();

            localDate = localDate.plusDays(14L);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date date = sdf.parse(localDate.getDayOfMonth() + "/" + localDate.getMonthValue() + "/" + localDate.getYear());

            book.setReturnDate(date);
            book.setAvailable(false);

            User user = findUserById(id);

            user.getBooks().add(book);

            mongoTemplate.save(user);
            mongoTemplate.save(book);
            return book;
        } else return null;
    }

    @Override
    public void addPhoto(String id, MultipartFile file) throws IOException {
        User user = findUserById(id);

        user.setImage(
                new Binary(BsonBinarySubType.BINARY, file.getBytes())
        );

        mongoTemplate.save(user);
    }

}
