package org.example.webshopbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.webshopbackend.dto.user.AuthResponseDTO;
import org.example.webshopbackend.dto.user.CreateUserDTO;
import org.example.webshopbackend.dto.user.LoginDTO;
import org.example.webshopbackend.exception.user.UsernameAlreadyExistsException;
import org.example.webshopbackend.exception.user.UsernameNotFoundForLoginException;
import org.example.webshopbackend.mapper.UserMapper;
import org.example.webshopbackend.model.User;
import org.example.webshopbackend.model.enums.Role;
import org.example.webshopbackend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTServiceImpl jwtService;

    public void createUser(CreateUserDTO dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UsernameAlreadyExistsException(dto.getUsername());
        }

        User userEntity = userMapper.mapCreateUserDTOToUserEntity(dto, Role.CUSTOMER);
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(userEntity);
    }

    @Override
    public AuthResponseDTO login(LoginDTO dto) {
        try {
            System.out.println("🔐 Attempting authentication for: " + dto.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            User user = (User) authentication.getPrincipal();
            System.out.println("✅ Authentication successful for: " + user.getUsername());
            String token = jwtService.generateToken();
            return new AuthResponseDTO(token, user.getId().toString(), user.getUsername(), user.getName());
        } catch (org.springframework.security.core.AuthenticationException e) {
            System.err.println("❌ Authentication failed: " + e.getMessage());
            System.err.println("❌ Exception type: " + e.getClass().getName());
            throw e;
        }
    }


}
