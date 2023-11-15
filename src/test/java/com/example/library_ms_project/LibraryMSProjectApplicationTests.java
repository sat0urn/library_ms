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
class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @Autowired
    BookService bookService;
    @Test
    public void checkAdminIS()
    {
        User user = userService.findUserByEmail("zeyin03@gmail.com");
        Assertions.assertEquals(user.getName(),"zeiin");
    }

/*    @Test
    public  void checkMethodOfUserRepository()
    {
        String id = "63f376e7f9980f60b222227e";
        User user = userService.findUserById(id);
        System.out.println(user.getName());
    }*/
    /*    @Test
        public void testBookingBack()
        {
            String id = "65493a1e194b6256eaf1d4fa";
            String bookID = "63f3911b6a0f41032a056f49";
            boolean res = userService.borrowBook(id,bookID);
            Assertions.assertTrue(res);
        }*/



}
