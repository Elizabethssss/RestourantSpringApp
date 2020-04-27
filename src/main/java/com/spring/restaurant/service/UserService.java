package com.spring.restaurant.service;

import com.spring.restaurant.domain.User;
import org.springframework.security.authentication.AuthenticationProvider;

import java.util.Optional;

public interface UserService extends AuthenticationProvider {
    boolean register(User user);

    Optional<User> findByEmail(String email);
}
