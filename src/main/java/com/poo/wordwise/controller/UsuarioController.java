package com.poo.wordwise.controller;

import com.poo.wordwise.dto.UsuarioDTO;
import com.poo.wordwise.service.intefaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private IUsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(this.usuarioService.registrarUsuario(usuarioDTO));
    }

    @DeleteMapping()
    public ResponseEntity<UsuarioDTO> deleteUsuario(@RequestHeader("idUsuario") Long idUsuario) {
        return ResponseEntity.ok(this.usuarioService.deleteUsuario(idUsuario));
    }

    @Autowired
    public void setUsuarioService(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
}
