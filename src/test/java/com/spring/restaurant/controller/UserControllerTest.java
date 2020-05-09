package com.spring.restaurant.controller;

import com.spring.restaurant.domain.Role;
import com.spring.restaurant.domain.User;
import com.spring.restaurant.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    private static final User USER = getUser();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void loginPage() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorization"));
    }

    @Test
    public void loginPageShouldHaveErrors() throws Exception {
        mvc.perform(get("/"))
//                .andExpect()
                .andExpect(status().isOk())
                .andExpect(view().name("authorization"));
    }

    @Test
    public void signUp() {
    }

    @Test
    public void testSignUp() {
    }

    private static User getUser() {
        final User user = new User();
        user.setId(1L);
        user.setUsername("Username");
        user.setEmail("Email");
        user.setPassword("Password");
        user.setRole(Role.USER);
        return user;
    }
}