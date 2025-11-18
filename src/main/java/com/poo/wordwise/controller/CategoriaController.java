package com.poo.wordwise.controller;

import com.poo.wordwise.dto.CategoriaDTO;
import com.poo.wordwise.service.intefaces.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/categoria")
public class CategoriaController {

    private ICategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<Page<CategoriaDTO>> findAllByIdUsuario(@RequestHeader(name = "idUsuario") Long idUsuario, Pageable pageable) {
        Page<CategoriaDTO> categorias = this.categoriaService.findAllByIdUsuario(idUsuario, pageable);
        if (categorias.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(categorias);
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> createCategoria(@RequestPart("categoria") CategoriaDTO categoriaDTO, @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        CategoriaDTO categoria = this.categoriaService.crearCategoria(categoriaDTO, imagen);
        return new  ResponseEntity<>(categoria, HttpStatus.CREATED);
    }

    @Autowired
    public void setCategoriaService(ICategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }
}
