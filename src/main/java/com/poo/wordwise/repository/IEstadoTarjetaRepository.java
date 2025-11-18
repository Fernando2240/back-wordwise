package com.poo.wordwise.repository;

import com.poo.wordwise.model.EstadoTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadoTarjetaRepository extends JpaRepository<EstadoTarjeta, Long> {
    EstadoTarjeta findBySigla(String sigla);
}
