package com.poo.wordwise.mapper;

import com.poo.wordwise.dto.CategoriaDTO;
import com.poo.wordwise.model.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoriaMapper {
    CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);

    @Mapping(target = "nombreUsuario", source = "usuario.email")
    CategoriaDTO toDto(Categoria categoria);

    Categoria toEntity(CategoriaDTO categoriaDTO);
}
