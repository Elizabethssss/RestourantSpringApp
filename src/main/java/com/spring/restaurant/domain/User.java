package com.spring.restaurant.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Domain class of User
 */

@Getter
@Setter
@NoArgsConstructor
public class User {
    private Long id;

    @NotBlank(message = "Username is required!")
    private String username;

    @Email(message = "Wrong email! Use only Aa-Zz, 0-9, ._@")
    @NotBlank(message = "Email is required!")
    private String email;

    @NotBlank(message = "Passwords can't be empty!")
    private String password;

    @NotBlank(message = "Passwords can't be empty!")
    private String rePassword;

    private Role role;
    private List<Order> orders;
}
