package com.poo.wordwise.controller;

import com.poo.wordwise.dto.DatosJWT;
import com.poo.wordwise.dto.UsuarioDTO;
import com.poo.wordwise.model.Usuario;
import com.poo.wordwise.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutenticationController {
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<DatosJWT> autenticarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        Authentication authToken = new UsernamePasswordAuthenticationToken(usuarioDTO.getUsername(), usuarioDTO.getPassword());
        var usuarioAutenticado = authenticationManager.authenticate(authToken);
        Usuario user = (Usuario) usuarioAutenticado.getPrincipal();
        var token = tokenService.generateToken(user);
        return ResponseEntity.ok(DatosJWT.builder().token(token).idUsuario(user.getId()).nombreUsuario(usuarioDTO.getUsername()).build());    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
