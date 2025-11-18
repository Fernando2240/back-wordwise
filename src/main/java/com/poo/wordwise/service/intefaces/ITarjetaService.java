package com.poo.wordwise.service.intefaces;

import com.poo.wordwise.dto.TarjetaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ITarjetaService {
    Page<TarjetaDTO> findAllByIdCategoria(Long idCategoria, Pageable pageable);

    TarjetaDTO createTarjeta(TarjetaDTO tarjetaDTO, MultipartFile imagen);

    Page<TarjetaDTO> findAllByIdCategoriaAndIdEstado(Long idCategoria, String estado, String query, Pageable pageable);

    Page<TarjetaDTO> findAllFavoritesByIdCategoria(Long idCategoria, String query, Pageable pageable);

    TarjetaDTO updateEstadoTarjeta(Long idTarjeta, String estado);

    TarjetaDTO deleteTarjeta(Long idTarjeta);

    TarjetaDTO updateTarjeta(TarjetaDTO tarjetaDTO, MultipartFile imagen);

    TarjetaDTO findTarjetaById(Long idTarjeta);
}
