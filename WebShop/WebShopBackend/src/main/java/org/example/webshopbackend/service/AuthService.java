package org.example.webshopbackend.service;

import org.example.webshopbackend.dto.user.AuthResponseDTO;
import org.example.webshopbackend.dto.user.CreateUserDTO;
import org.example.webshopbackend.dto.user.LoginDTO;

public interface AuthService {

    void createUser(CreateUserDTO dto);
    AuthResponseDTO login(LoginDTO dto);

}
