package com.poo.wordwise.mapper;

import com.poo.wordwise.dto.TarjetaDTO;
import com.poo.wordwise.model.Tarjeta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TarjetaMapper {
    TarjetaMapper INSTANCE = Mappers.getMapper(TarjetaMapper.class);

    @Mapping(target = "nombreCategoria", source = "categoria.nombre")
    @Mapping(target = "siglaEstado", source = "estadoTarjeta.sigla")
    TarjetaDTO toDto(Tarjeta tarjeta);

    @Mapping(target = "esFavorita", source = "esFavorita")
    Tarjeta toEntity(TarjetaDTO tarjetaDTO);
}
