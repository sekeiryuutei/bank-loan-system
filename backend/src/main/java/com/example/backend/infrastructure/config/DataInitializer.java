package com.example.backend.infrastructure.config;

import com.example.backend.domain.model.Role;
import com.example.backend.domain.model.User;
import com.example.backend.infrastructure.persistence.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("usuario@test.com")) {
            User user = User.builder()
                    .email("usuario@test.com")
                    .password(passwordEncoder.encode("123"))
                    .role(Role.ROLE_USER)
                    .build();
            userRepository.save(user);
        }

        if (!userRepository.existsByEmail("admin@test.com")) {
            User admin = User.builder()
                    .email("admin@test.com")
                    .password(passwordEncoder.encode("123"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }
}
