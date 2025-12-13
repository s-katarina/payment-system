package org.example.pspbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pspbackend.dto.auth.LoginRequestDTO;
import org.example.pspbackend.dto.auth.LoginResponseDTO;
import org.example.pspbackend.model.Admin;
import org.example.pspbackend.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("=== LOGIN ATTEMPT ===");
        log.info("Username: {}", request.getUsername());
        log.info("Total admins in repository: {}", adminRepository.count());
        
        // List all usernames for debugging
        adminRepository.findAll().forEach(a -> 
            log.info("Admin in DB: username={}, active={}, id={}", a.getUsername(), a.isActive(), a.getId())
        );
        
        Admin admin = adminRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.error("=== LOGIN FAILED: USER NOT FOUND ===");
                    log.error("Username attempted: {}", request.getUsername());
                    log.error("Total admins in DB: {}", adminRepository.count());
                    adminRepository.findAll().forEach(a -> 
                        log.error("Existing admin: username={}", a.getUsername())
                    );
                    return new RuntimeException("Invalid username or password");
                });

        log.info("Admin found: username={}, active={}, id={}", admin.getUsername(), admin.isActive(), admin.getId());
        log.info("Stored password hash (first 30 chars): {}", 
            admin.getPassword() != null ? admin.getPassword().substring(0, Math.min(30, admin.getPassword().length())) : "NULL");

        if (!admin.isActive()) {
            log.error("=== LOGIN FAILED: ACCOUNT INACTIVE ===");
            log.error("Username: {}", request.getUsername());
            throw new RuntimeException("Account is inactive");
        }

        log.info("Attempting password match...");
        log.info("Input password: {}", request.getPassword());
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), admin.getPassword());
        log.info("Password match result: {}", passwordMatches);
        
        if (!passwordMatches) {
            log.error("=== LOGIN FAILED: PASSWORD MISMATCH ===");
            log.error("Username: {}", request.getUsername());
            log.error("Input password: {}", request.getPassword());
            log.error("Stored hash: {}", admin.getPassword());
            // Test if encoding works
            String testHash = passwordEncoder.encode(request.getPassword());
            log.error("New hash for input password would be: {}", testHash);
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtService.generateToken(admin.getUsername(), "ADMIN");
        
        log.info("Login successful for user: {}", request.getUsername());
        
        LoginResponseDTO.UserDTO userDTO = new LoginResponseDTO.UserDTO(
                admin.getId(),
                admin.getUsername(),
                admin.getName()
        );
        
        return new LoginResponseDTO(token, "Bearer", userDTO);
    }
}

