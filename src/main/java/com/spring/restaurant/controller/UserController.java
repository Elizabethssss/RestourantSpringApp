package com.spring.restaurant.controller;

import com.spring.restaurant.domain.User;
import com.spring.restaurant.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    public static final String RIGHT_SIDE = "rightSide";
    public static final String AUTHORIZATION = "authorization";
    private final UserService userService;

    @GetMapping("/")
    public String loginPage(@ModelAttribute User user,
                            @RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Incorrect email or password!");
        }
        model.addAttribute(RIGHT_SIDE, false);
        return AUTHORIZATION;
    }

    @GetMapping("/signUp")
    public String signUp(Model model, @ModelAttribute User user) {
        model.addAttribute(RIGHT_SIDE, true);
        model.addAttribute("user", new User());
        return AUTHORIZATION;
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute @Valid User user, Errors errors, Model model) {
        Optional<User> optionalUser = userService.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            errors.rejectValue("email","error.user", "Email is already used!");
        }
        final String password = user.getPassword();
        if (password != null && !password.equals(user.getRePassword())) {
            errors.rejectValue("rePassword", "error.user", "Passwords are not equal!");
        }
        if (errors.hasErrors()) {
            model.addAttribute(RIGHT_SIDE, true);
            return AUTHORIZATION;
        }
        userService.register(user);
        return "redirect:/";
    }
}
