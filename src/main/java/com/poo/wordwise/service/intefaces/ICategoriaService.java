package com.poo.wordwise.service.intefaces;

import com.poo.wordwise.dto.CategoriaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ICategoriaService {
    Page<CategoriaDTO> findAllByIdUsuario(Long idUsuario, Pageable pageable);

    CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO, MultipartFile imagen);
}
