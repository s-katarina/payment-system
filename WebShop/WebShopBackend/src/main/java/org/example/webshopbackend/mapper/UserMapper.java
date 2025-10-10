package org.example.webshopbackend.mapper;

import lombok.RequiredArgsConstructor;
import org.example.webshopbackend.dto.user.CreateUserDTO;
import org.example.webshopbackend.model.User;
import org.example.webshopbackend.model.enums.Role;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper {

    public User mapCreateUserDTOToUserEntity(CreateUserDTO dto, Role role) {
        User userEntity = new User();
        userEntity.setName(dto.getName());
        userEntity.setUsername(dto.getUsername());
        userEntity.setPassword(dto.getPassword());
        userEntity.setRole(role);
        return userEntity;
    }

}
