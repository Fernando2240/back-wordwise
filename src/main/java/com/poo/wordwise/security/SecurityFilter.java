package com.poo.wordwise.security;

import com.poo.wordwise.repository.IUsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private TokenService tokenService;
    private IUsuarioRepository usuarioReposirory;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Se obtiene el token de autenticación
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            String subject = this.tokenService.verifyToken(token);
            if (subject != null) {
                UserDetails usuario = usuarioReposirory.findByEmailAndActivoTrue(subject);
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null,
                        usuario.getAuthorities()); //Forzamos un inicio de sesion
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setUsuarioReposirory(IUsuarioRepository usuarioReposirory) {
        this.usuarioReposirory = usuarioReposirory;
    }
}
