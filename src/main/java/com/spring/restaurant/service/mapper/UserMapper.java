package com.spring.restaurant.service.mapper;

import com.spring.restaurant.domain.Role;
import com.spring.restaurant.domain.User;
import com.spring.restaurant.entity.RoleEntity;
import com.spring.restaurant.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserEntity> {
    @Override
    public User mapEntityToDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        User user = new User();
        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setRole(Role.valueOf(entity.getRoleEntity().name()));
        return user;
    }

    @Override
    public UserEntity mapDomainToEntity(User domain) {
        if (domain == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(domain.getId());
        userEntity.setUsername(domain.getUsername());
        userEntity.setEmail(domain.getEmail());
        userEntity.setPassword(domain.getPassword());
        userEntity.setRoleEntity(domain.getRole() == null ? RoleEntity.USER : RoleEntity.valueOf(domain.getRole().name()));
        return userEntity;
    }
}
