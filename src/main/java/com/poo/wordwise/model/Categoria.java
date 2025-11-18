package com.poo.wordwise.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "categorias", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Categoria implements Serializable {

    @Serial
    private static final long serialVersionUID = 262626662555L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    //Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;
}
