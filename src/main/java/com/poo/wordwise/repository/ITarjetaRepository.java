package com.poo.wordwise.repository;

import com.poo.wordwise.model.Tarjeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITarjetaRepository extends JpaRepository<Tarjeta, Long> {
    boolean existsByPalabraAndIdCategoria(String palabra, Long idCategoria);

    @Query("SELECT t FROM Tarjeta t WHERE t.idCategoria =:idCategoria " +
            "AND (:idEstado IS NULL OR t.idEstado =:idEstado) " +
            "AND (:query IS NULL " +
            "   OR UPPER(TRANSLATE(t.palabra, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')) LIKE :query " +
            "   OR UPPER(TRANSLATE(t.traduccion, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')) LIKE :query )")
    Page<Tarjeta> findAllByIdCategoriaAndIdEstado(Long idCategoria, Long idEstado, String query, Pageable pageable);

    Page<Tarjeta> findAllByIdCategoria(Long idCategoria, Pageable pageable);

    @Query("SELECT t FROM Tarjeta t WHERE t.idCategoria =:idCategoria " +
            "AND t.esFavorita IS TRUE  " +
            "AND (:query IS NULL " +
            "   OR UPPER(TRANSLATE(t.palabra, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')) LIKE :query " +
            "   OR UPPER(TRANSLATE(t.traduccion, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')) LIKE :query )")
    Page<Tarjeta> findAllFavoritesByIdCategoria(Long idCategoria, String query, Pageable pageable);
}
