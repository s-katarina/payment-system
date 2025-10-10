package org.example.webshopbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.webshopbackend.dto.user.AuthResponseDTO;
import org.example.webshopbackend.dto.user.CreateUserDTO;
import org.example.webshopbackend.dto.user.LoginDTO;
import org.example.webshopbackend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/register")
    public ResponseEntity<Void> registerUser(@RequestBody CreateUserDTO dto) {
        authService.createUser(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO dto) {
        return new ResponseEntity<>(authService.login(dto), HttpStatus.OK);
    }


}
