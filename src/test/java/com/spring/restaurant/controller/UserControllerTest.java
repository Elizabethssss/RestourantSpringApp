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

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void loginShouldReturnString() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorization"));
    }

    @Test
    public void loginPageShouldHaveErrors() throws Exception {
        mvc.perform(get("/?error=1"))
//                .andExpect()
                .andExpect(status().isOk())
                .andExpect(view().name("authorization"));
    }

    @Test
    public void signUpShouldReturnString() throws Exception {
        mvc.perform(get("/signUp"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorization"));
    }

    @Test
    public void registrationShouldBeOk() throws Exception {
        mvc.perform(post("/signUp")
                .param("username", "username")
                .param("email", "email@mail.com")
                .param("password", "password")
                .param("rePassword", "password")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    public void registrationShouldNotPass() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));

        mvc.perform(post("/signUp")
                .param("username", "username")
                .param("email", "email@mail.com")
                .param("password", "password")
                .param("rePassword", "password2")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("authorization"));
    }

    @Test
    public void registrationShouldNotPass2() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));

        mvc.perform(post("/signUp")
                .param("username", "username")
                .param("email", "email@mail.com")
                .param("password", "")
                .param("rePassword", "password2")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("authorization"));
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