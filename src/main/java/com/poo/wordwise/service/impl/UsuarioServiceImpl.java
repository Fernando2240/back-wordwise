package com.poo.wordwise.service.impl;

import com.poo.wordwise.dto.UsuarioDTO;
import com.poo.wordwise.model.Usuario;
import com.poo.wordwise.repository.IUsuarioRepository;
import com.poo.wordwise.service.intefaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    private IUsuarioRepository userRepository;

    @Override
    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) {
        //Verificamos que el usuario no exista
        boolean existeUsuario = this.userRepository.existsByEmail(usuarioDTO.getUsername());
        if (existeUsuario) throw new IllegalArgumentException("Ya existe un usuario con el email " + usuarioDTO.getUsername());
        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioDTO.getUsername());
        usuario.setContrasenna(usuarioDTO.getPassword());
        usuario.setActivo(true);
        this.userRepository.save(usuario);
        usuarioDTO.setPassword(null);
        return usuarioDTO;
    }

    @Override
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
}
