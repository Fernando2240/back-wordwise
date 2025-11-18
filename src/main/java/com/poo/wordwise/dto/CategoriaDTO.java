package com.poo.wordwise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 45626288L;

    private Long id;

    private Long idUsuario;

    private String nombreUsuario;

    private String nombre;

    private String descripcion;

    private String imagen;
}
