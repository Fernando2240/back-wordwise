package com.poo.wordwise.repository;

import com.poo.wordwise.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByEmailAndActivoTrue(String subject);

    boolean existsByEmail(String username);
}
