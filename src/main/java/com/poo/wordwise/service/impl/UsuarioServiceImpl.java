package com.poo.wordwise.service.impl;

import com.poo.wordwise.dto.UsuarioDTO;
import com.poo.wordwise.exception.WordWiseValidationException;
import com.poo.wordwise.model.Usuario;
import com.poo.wordwise.repository.IUsuarioRepository;
import com.poo.wordwise.service.intefaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    private IUsuarioRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) {
        //Verificamos que el usuario no exista
        boolean existeUsuario = this.userRepository.existsByEmail(usuarioDTO.getUsername());
        if (existeUsuario) throw new WordWiseValidationException("Ya existe un usuario con el email " + usuarioDTO.getUsername());
        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioDTO.getUsername());
        usuario.setContrasenna(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setActivo(true);
        this.userRepository.save(usuario);
        usuarioDTO.setPassword(null);
        return usuarioDTO;
    }

    @Override
    @Transactional
    public UsuarioDTO deleteUsuario(Long idUsuario) {
        //Buscamos el usuario a eliminar
        Usuario usuario = this.userRepository.getReferenceById(idUsuario);
        usuario.setActivo(false);
        this.userRepository.save(usuario);
        return new UsuarioDTO(usuario.getEmail(), null);
    }

    @Autowired
    public void setUserRepository(IUsuarioRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
