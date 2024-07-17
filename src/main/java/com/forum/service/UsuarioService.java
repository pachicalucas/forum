package com.forum.service;

import com.forum.entity.Usuario;
import com.forum.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario cadastrarUsuario(Usuario usuario) {
        logger.info("Tentativa de cadastro de novo usuário: {}", usuario.getUsername());

        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            logger.warn("Tentativa de cadastro com username já existente: {}", usuario.getUsername());
            throw new RuntimeException("Username já existe");
        }

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            logger.warn("Tentativa de cadastro com email já existente: {}", usuario.getEmail());
            throw new RuntimeException("Email já existe");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        logger.info("Novo usuário cadastrado com sucesso: {}", usuarioSalvo.getUsername());

        return usuarioSalvo;
    }

    public Usuario findByUsernameOrEmail(String usernameOrEmail) {
        return usuarioRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElse(null);
    }
}