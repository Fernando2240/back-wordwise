package com.poo.wordwise.controller;

import com.poo.wordwise.dto.TarjetaDTO;
import com.poo.wordwise.service.intefaces.ITarjetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tarjeta")
public class TarjetaController {

    private ITarjetaService tarjetaService;

    @GetMapping("/{idCategoria}")
    public ResponseEntity<Page<TarjetaDTO>> findAllByIdCategoria(@PathVariable("idCategoria") Long idCategoria, @PageableDefault Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),  1);
        Page<TarjetaDTO> targetas = this.tarjetaService.findAllByIdCategoria(idCategoria, pageable);
        if(targetas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(targetas);
    }

    @GetMapping("/{idCategoria}/{estado}")
    public ResponseEntity<Page<TarjetaDTO>> findAllByIdCategoriaAndIdEstado(@PathVariable("idCategoria") Long idCategoria, @PathVariable("estado") String estado, @RequestParam(value = "query", required = false) String query, @PageableDefault Pageable pageable) {
        Page<TarjetaDTO> tarjetas = this.tarjetaService.findAllByIdCategoriaAndIdEstado(idCategoria, estado, query, pageable);
        if(tarjetas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(tarjetas);
    }

    @PostMapping("/create")
    public ResponseEntity<TarjetaDTO> createTarjeta(@RequestPart("tarjeta") TarjetaDTO tarjetaDTO, @RequestPart("imagen") MultipartFile imagen) {
        TarjetaDTO tarjeta = this.tarjetaService.createTarjeta(tarjetaDTO, imagen);
        return ResponseEntity.ok(tarjeta);
    }

    @PutMapping("/favorite/{idTarjeta}")
    public ResponseEntity<TarjetaDTO> addToFavorites(@PathVariable("idTarjeta") Long idTarjeta){
        return ResponseEntity.ok(this.tarjetaService.addToFavorites(idTarjeta));
    }

    @Autowired
    public void setTarjetaService(ITarjetaService tarjetaService) {
        this.tarjetaService = tarjetaService;
    }
}
