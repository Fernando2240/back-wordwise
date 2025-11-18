package com.poo.wordwise.service.impl;

import com.poo.wordwise.dto.TarjetaDTO;
import com.poo.wordwise.mapper.TarjetaMapper;
import com.poo.wordwise.model.EstadoTarjeta;
import com.poo.wordwise.model.Tarjeta;
import com.poo.wordwise.repository.IEstadoTarjetaRepository;
import com.poo.wordwise.repository.ITarjetaRepository;
import com.poo.wordwise.service.intefaces.ITarjetaService;
import com.poo.wordwise.util.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TarjetaServiceImpl implements ITarjetaService {

    private static final Logger log = LoggerFactory.getLogger(TarjetaServiceImpl.class);
    private ITarjetaRepository tajetaRepository;
    private IEstadoTarjetaRepository estadoTarjetaRepository;
    private CloudinaryService cloudinaryService;

    @Override
    public Page<TarjetaDTO> findAllByIdCategoria(Long idCategoria, Pageable pageable) {
        Page<Tarjeta> tarjetas = this.tajetaRepository.findAllByIdCategoria(idCategoria, pageable);
        return new PageImpl<>(tarjetas.map(TarjetaMapper.INSTANCE::toDto).stream().toList(),  pageable, tarjetas.getTotalElements());
    }

    @Override
    public TarjetaDTO createTarjeta(TarjetaDTO tarjetaDTO, MultipartFile imagen) {
        //Se valida la existencia de la tarjeta
        boolean existeTarjeta = this.tajetaRepository.existsByPalabraAndIdCategoria(tarjetaDTO.getPalabra(), tarjetaDTO.getIdCategoria());
        if (existeTarjeta) throw new IllegalArgumentException("Ya existe la palabra para esta categoria");

        //Se asigna el estado POR REPASAR
        EstadoTarjeta estadoRepasar = getEstadoTarjeta("REP");
        Tarjeta tarjeta = TarjetaMapper.INSTANCE.toEntity(tarjetaDTO);

        String url = null;
        if(imagen != null && !imagen.isEmpty()){
            try {
                url = this.cloudinaryService.uploadImage(imagen, "tarjetas");
            } catch (IOException e) {
                log.info(e.getMessage());
            }
            tarjeta.setImagen(url);
        }

        tarjeta.setIdEstado(estadoRepasar.getId());
        tarjeta.setEstadoTarjeta(estadoRepasar);
        return TarjetaMapper.INSTANCE.toDto(this.tajetaRepository.save(tarjeta));
    }

    @Override
    public Page<TarjetaDTO> findAllByIdCategoriaAndIdEstado(Long idCategoria, String estado, String query, Pageable pageable) {
        //Se busca el estado
        EstadoTarjeta estadoTarjeta = estado != null ? this.estadoTarjetaRepository.findBySigla(estado.trim()) : null;
        Page<Tarjeta> tarjetas = this.tajetaRepository.findAllByIdCategoriaAndIdEstado(idCategoria, estadoTarjeta != null ? estadoTarjeta.getId() : null, cleanValue(query), pageable);
        return new PageImpl<>(tarjetas.map(TarjetaMapper.INSTANCE::toDto).stream().toList(),  pageable, tarjetas.getTotalElements());
    }

    @Override
    public TarjetaDTO addToFavorites(Long idTarjeta) {
        //Buscamos el estado de favoritos
        EstadoTarjeta estadoFavoritos = getEstadoTarjeta("FAV");
        //Buscamos la targeta
        Tarjeta tarjeta = this.tajetaRepository.getReferenceById(idTarjeta);
        tarjeta.setIdEstado(estadoFavoritos.getId());
        tarjeta.setEstadoTarjeta(estadoFavoritos);
        return TarjetaMapper.INSTANCE.toDto(this.tajetaRepository.save(tarjeta));
    }

    private EstadoTarjeta getEstadoTarjeta(String siglaEstado){
        //Buscamos el estado de favoritos
        EstadoTarjeta estado = this.estadoTarjetaRepository.findBySigla(siglaEstado);
        if(estado == null) throw new IllegalArgumentException("No se encontro el estado");
        return estado;
    }

    private String removeExtraSpaces(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Eliminar espacios al principio, al final y dobles
        return input.trim().replaceAll("\\s+", " ");
    }

    private String convertToUpperCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String trimmedInput = removeExtraSpaces(input);
        return trimmedInput.toUpperCase();
    }

    private String cleanValue(String value){
        if (value != null) {
            value = convertToUpperCase(value);
            if (value.isEmpty()) {
                value = null;
            }else{
                value = "%" + value + "%";
            }
        }
        return value;
    }

    @Autowired
    public void setTajetaRepository(ITarjetaRepository tajetaRepository) {
        this.tajetaRepository = tajetaRepository;
    }

    @Autowired
    public void setEstadoTarjetaRepository(IEstadoTarjetaRepository estadoTarjetaRepository) {
        this.estadoTarjetaRepository = estadoTarjetaRepository;
    }

    @Autowired
    public void setCloudinaryService(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }
}
