package com.poo.wordwise.service.intefaces;

import com.poo.wordwise.dto.CategoriaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICategoriaService {
    List<CategoriaDTO> findAllByIdUsuario(Long idUsuario);

    CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO, MultipartFile imagen);
}
