package com.example.serverless.serverless.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.example.serverless.serverless.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.serverless.serverless.repository.UserRepository;
import com.example.serverless.serverless.service.JwtService;
import com.example.serverless.serverless.dto.AuthenticationRequest;
import com.example.serverless.serverless.dto.AuthenticationResponse;
import com.example.serverless.serverless.dto.RegisterRequest;
import com.example.serverless.serverless.entity.User.Role;

import lombok.RequiredArgsConstructor;
import lombok.experimental.var;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        var token = jwtService.generateToken(user);
        
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CLIENTE);
            
        userRepository.save(user);
        
        var token = jwtService.generateToken(user);
        
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}