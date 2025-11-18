package com.poo.wordwise.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.poo.wordwise.exception.WordWiseUnauthorizedException;
import com.poo.wordwise.exception.WordWiseValidationException;
import com.poo.wordwise.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    @Value("${security.apiKey}")
    private String apiKey;

    /**
     * Genera un token JWT para un usuario
     */
    public String generateToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiKey);
            return JWT.create()
                    .withIssuer("wordwise")
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);

        } catch (JWTCreationException e) {
            throw new WordWiseValidationException("Error generando token JWT", e);
        }
    }

    /**
     * Verifica y extrae el email del token
     * Lanza 401 si el token es inválido, expirado o falta
     */
    public String verifyToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new WordWiseUnauthorizedException("Token no proporcionado");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(apiKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("wordwise")
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);

            String subject = decodedJWT.getSubject();
            if (subject == null || subject.isEmpty()) {
                throw new WordWiseUnauthorizedException("Token no contiene información del usuario");
            }

            return subject;

        } catch (TokenExpiredException e) {
            throw new WordWiseUnauthorizedException("El token ha expirado", e);

        } catch (JWTVerificationException e) {
            throw new WordWiseUnauthorizedException("Token inválido o manipulado", e);
        }
    }

    /**
     * Establece una duración de 2 horas
     */
    private Instant generarFechaExpiracion() {
        return Instant.now().plusSeconds(2L * 3600L);
    }
}
