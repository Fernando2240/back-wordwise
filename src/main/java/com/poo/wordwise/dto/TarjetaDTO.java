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
public class TarjetaDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 45125256216262L;

    private Long id;
    private Long idCategoria;
    private String nombreCategoria;
    private String palabra;
    private String traduccion;
    private String imagen;
    private String siglaEstado;
}
