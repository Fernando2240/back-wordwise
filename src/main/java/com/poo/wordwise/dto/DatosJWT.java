package com.poo.wordwise.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DatosJWT implements Serializable {

    @Serial
    private static final long serialVersionUID = -459596262615L;

    private String token;
    private Long idUsuario;
    private String nombreUsuario;

}
