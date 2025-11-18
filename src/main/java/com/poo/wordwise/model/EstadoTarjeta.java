package com.poo.wordwise.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.print.attribute.standard.MediaSize;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "estados_tarjeta", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoTarjeta implements Serializable {
    @Serial
    private static final long serialVersionUID = 885929951L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "sigla")
    private String sigla;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_desde")
    private LocalDateTime fechaDesde;

    @Column(name = "fecha_hasta")
    private LocalDateTime fechaHasta;
}
