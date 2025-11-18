package com.poo.wordwise.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = 4151651551L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "contrasenna")
    private String contrasenna;

    @Column(name = "activo")
    private boolean activo;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.contrasenna;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
