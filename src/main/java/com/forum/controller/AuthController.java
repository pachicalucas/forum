package com.forum.controller;

import com.forum.dto.JwtAuthenticationResponse;
import com.forum.dto.LoginRequest;
import com.forum.entity.Usuario;
import com.forum.security.JwtTokenManager;
import com.forum.security.JwtTokenProvider;
import com.forum.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)

public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody Usuario usuario) {
        System.out.println("AuthController: Received signup request for user: " + usuario.getUsername());
        try {
            Usuario result = usuarioService.cadastrarUsuario(usuario);
            System.out.println("AuthController: User registered successfully: " + result.getUsername());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("AuthController: Error registering user: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("Received login request: " + loginRequest);
        System.out.println("Received login request for user: " + loginRequest.getUsername());

        Usuario usuario = usuarioService.findByUsernameOrEmail(loginRequest.getUsername());
        if (usuario == null) {
            System.out.println("User not found: " + loginRequest.getUsername());
            return ResponseEntity.badRequest().body("Error: User not found");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenManager.generateToken(authentication);

            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        } catch (BadCredentialsException e) {
            System.out.println("Bad credentials for user: " + loginRequest.getUsername());
            return ResponseEntity.badRequest().body("Error: Invalid username or password");
        }
    }
}