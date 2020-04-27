package com.spring.restaurant.service.mapper;

import com.spring.restaurant.domain.Role;
import com.spring.restaurant.domain.User;
import com.spring.restaurant.entity.RoleEntity;
import com.spring.restaurant.entity.UserEntity;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class UserMapperTest {
    private static final User USER = getUser();
    private static final UserEntity USER_ENTITY = getUserEntity();

    private Mapper<User, UserEntity> mapper = new UserMapper();

    @Test
    public void mapEntityToDomain() {
        final User user = mapper.mapEntityToDomain(USER_ENTITY);

        assertThat(user.getId(), is(USER_ENTITY.getId()));
        assertThat(user.getUsername(), is(USER_ENTITY.getUsername()));
        assertThat(user.getEmail(), is(USER_ENTITY.getEmail()));
        assertThat(user.getPassword(), is(USER_ENTITY.getPassword()));
        assertThat(user.getRole(), is(Role.USER));
    }

    @Test
    public void mapEntityToDomainShouldReturnNull() {
        final User user = mapper.mapEntityToDomain(null);
        assertNull(user);
    }

    @Test
    public void mapDomainToEntity() {
        final UserEntity userEntity = mapper.mapDomainToEntity(USER);

        assertThat(userEntity.getId(), is(USER.getId()));
        assertThat(userEntity.getUsername(), is(USER.getUsername()));
        assertThat(userEntity.getEmail(), is(USER.getEmail()));
        assertThat(userEntity.getPassword(), is(USER.getPassword()));
        assertThat(userEntity.getRoleEntity(), is(RoleEntity.USER));
    }

    @Test
    public void mapDomainToEntityWithNullRole() {
        USER.setRole(null);
        final UserEntity userEntity = mapper.mapDomainToEntity(USER);

        assertThat(userEntity.getId(), is(USER.getId()));
        assertThat(userEntity.getUsername(), is(USER.getUsername()));
        assertThat(userEntity.getEmail(), is(USER.getEmail()));
        assertThat(userEntity.getPassword(), is(USER.getPassword()));
        assertThat(userEntity.getRoleEntity(), is(RoleEntity.USER));
    }

    @Test
    public void mapDomainToEntityShouldReturnNull() {
        final UserEntity userEntity = mapper.mapDomainToEntity(null);
        assertNull(userEntity);
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