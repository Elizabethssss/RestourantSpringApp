package com.spring.restaurant.service.impl;

import com.spring.restaurant.domain.Role;
import com.spring.restaurant.domain.User;
import com.spring.restaurant.entity.RoleEntity;
import com.spring.restaurant.entity.UserEntity;
import com.spring.restaurant.repository.UserRepository;
import com.spring.restaurant.service.mapper.Mapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    private static final User USER = getUser();
    private static final UserEntity USER_ENTITY = getUserEntity();

    @Mock
    private UserRepository userRepository;
    @Mock
    private Mapper<User, UserEntity> userMapper;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private UserServiceImpl userService;

    @After
    public void reset() {
        Mockito.reset(userRepository, userMapper);
    }

    @Test
    public void registerShouldReturnFalse() {
        when(userRepository.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER_ENTITY));
        when(userMapper.mapEntityToDomain(USER_ENTITY)).thenReturn(USER);

        assertFalse(userService.register(USER));

        verify(userRepository).findByEmail(USER.getEmail());
        verify(userMapper).mapEntityToDomain(USER_ENTITY);
    }

    @Test
    public void registerShouldReturnTrue() {
        when(userRepository.findByEmail(USER.getEmail())).thenReturn(Optional.empty());
        when(userMapper.mapDomainToEntity(USER)).thenReturn(USER_ENTITY);
        when(userRepository.save(USER_ENTITY)).thenReturn(USER_ENTITY);

        assertTrue(userService.register(USER));

        verify(userRepository).findByEmail(USER.getEmail());
        verify(userMapper).mapDomainToEntity(USER);
        verify(userRepository).save(USER_ENTITY);
    }


    @Test
    public void findByEmail() {
        when(userRepository.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER_ENTITY));
        when(userMapper.mapEntityToDomain(USER_ENTITY)).thenReturn(USER);

        Optional<User> user = userService.findByEmail(USER.getEmail());

        assertTrue(user.isPresent());
        verify(userRepository).findByEmail(USER.getEmail());
        verify(userMapper).mapEntityToDomain(USER_ENTITY);
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

    private static UserEntity getUserEntity() {
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("Username");
        userEntity.setEmail("Email");
        userEntity.setPassword("Password");
        userEntity.setRoleEntity(RoleEntity.USER);
        return userEntity;
    }
}