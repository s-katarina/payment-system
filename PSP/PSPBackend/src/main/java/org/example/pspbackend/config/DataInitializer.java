package org.example.pspbackend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pspbackend.model.Admin;
import org.example.pspbackend.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("DataInitializer: Starting admin user initialization...");
        log.info("DataInitializer: Total admins in DB: {}", adminRepository.count());
        
        // Create or update default admin
        adminRepository.findByUsername("admin").ifPresentOrElse(
            existingAdmin -> {
                log.info("Admin user already exists");
                log.info("Existing admin - ID: {}, Username: {}, Name: {}, Active: {}", 
                    existingAdmin.getId(), existingAdmin.getUsername(), existingAdmin.getName(), existingAdmin.isActive());
                
                // Verify password is correct, if not, update it
                if (!passwordEncoder.matches("admin123", existingAdmin.getPassword())) {
                    log.warn("Admin password hash doesn't match 'admin123', updating...");
                    String newHash = passwordEncoder.encode("admin123");
                    existingAdmin.setPassword(newHash);
                    existingAdmin.setActive(true);
                    adminRepository.save(existingAdmin);
                    log.info("Admin password updated successfully!");
                } else {
                    log.info("Admin password is correct");
                }
            },
            () -> {
                log.info("Creating default admin user...");
                try {
                    Admin admin = new Admin();
                    admin.setUsername("admin");
                    String hashedPassword = passwordEncoder.encode("admin123");
                    admin.setPassword(hashedPassword);
                    admin.setName("Admin User");
                    admin.setActive(true);
                    Admin saved = adminRepository.save(admin);
                    log.info("Default admin user created successfully!");
                    log.info("Admin ID: {}, Username: {}, Name: {}, Active: {}", 
                        saved.getId(), saved.getUsername(), saved.getName(), saved.isActive());
                    log.info("Password hash (first 30 chars): {}", hashedPassword.substring(0, Math.min(30, hashedPassword.length())));
                } catch (Exception e) {
                    log.error("Failed to create admin user: {}", e.getMessage(), e);
                }
            }
        );
        
        log.info("DataInitializer: Finished. Total admins in DB: {}", adminRepository.count());
    }
}

