package com.example.backend.infrastructure.web;

import com.example.backend.application.dto.AuthRequestDTO;
import com.example.backend.application.dto.AuthResponseDTO;
import com.example.backend.infrastructure.security.JwtService;
import com.example.backend.infrastructure.persistence.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody AuthRequestDTO authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        String token = jwtService.generateToken(authentication);
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        return new AuthResponseDTO(token, authentication.getName(), role);
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody AuthRequestDTO authRequest) {
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            throw new com.example.backend.domain.exception.InvalidLoanStateException("El correo ya se encuentra registrado");
        }
        com.example.backend.domain.model.User user = com.example.backend.domain.model.User.builder()
                .email(authRequest.getEmail())
                .password(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(authRequest.getPassword()))
                .role(com.example.backend.domain.model.Role.ROLE_USER)
                .build();
        userRepository.save(user);
        return "Usuario registrado de manera exitosa";
    }
}
