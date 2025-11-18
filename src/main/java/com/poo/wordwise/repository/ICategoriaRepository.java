package com.poo.wordwise.repository;

import com.poo.wordwise.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("SELECT c FROM Categoria c " +
            "WHERE c.idUsuario =:idUsuario ")
    Page<Categoria> findAllByIdUsuario(Long idUsuario, Pageable pageable);

    boolean existsByNombreAndIdUsuario(String trim, Long idUsuario);
}
