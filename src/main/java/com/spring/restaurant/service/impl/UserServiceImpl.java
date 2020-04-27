package com.spring.restaurant.service.impl;

import com.spring.restaurant.domain.User;
import com.spring.restaurant.entity.UserEntity;
import com.spring.restaurant.repository.UserRepository;
import com.spring.restaurant.service.UserService;
import com.spring.restaurant.service.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Mapper<User, UserEntity> userMapper;
    private final PasswordEncoder encoder;

    @Override
    public boolean register(User user) {
        if(findByEmail(user.getEmail()).isPresent()) {
            return false;
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(userMapper.mapDomainToEntity(user));
        return true;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::mapEntityToDomain);
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Incorrect email or password!"));

        if(encoder.matches(password, userEntity.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userEntity.getEmail(), userEntity.getPassword(), getAuthorities(userEntity));
        }
        throw new BadCredentialsException("Incorrect email or password!");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    private List<SimpleGrantedAuthority> getAuthorities(UserEntity userEntity) {
        return singletonList(new SimpleGrantedAuthority(userEntity.getRoleEntity().name()));
    }
}
