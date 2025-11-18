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
@Table(name = "tarjetas", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tarjeta implements Serializable {

    @Serial
    private static final long serialVersionUID = 848229652191L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "palabra")
    private String palabra;

    @Column(name = "traduccion")
    private String traduccion;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(name = "id_estado")
    private Long idEstado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    //Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria",  insertable = false, updatable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", insertable = false, updatable = false)
    private EstadoTarjeta estadoTarjeta;
}
