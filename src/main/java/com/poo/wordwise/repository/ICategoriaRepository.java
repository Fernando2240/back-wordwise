package com.poo.wordwise.repository;

import com.poo.wordwise.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("SELECT c FROM Categoria c " +
            "WHERE c.idUsuario =:idUsuario ")
    List<Categoria> findAllByIdUsuario(Long idUsuario);

    boolean existsByNombreAndIdUsuario(String trim, Long idUsuario);
}
