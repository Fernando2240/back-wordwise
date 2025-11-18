package com.poo.wordwise.service.intefaces;

import com.poo.wordwise.dto.UsuarioDTO;

public interface IUsuarioService {
    UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO);

    UsuarioDTO deleteUsuario(Long idUsuario);
}
