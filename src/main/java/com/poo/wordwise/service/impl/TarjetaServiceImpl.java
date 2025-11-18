package com.poo.wordwise.service.impl;

import com.poo.wordwise.dto.TarjetaDTO;
import com.poo.wordwise.exception.WordWiseDataNotFoundException;
import com.poo.wordwise.exception.WordWiseValidationException;
import com.poo.wordwise.mapper.TarjetaMapper;
import com.poo.wordwise.model.EstadoTarjeta;
import com.poo.wordwise.model.Tarjeta;
import com.poo.wordwise.repository.IEstadoTarjetaRepository;
import com.poo.wordwise.repository.ITarjetaRepository;
import com.poo.wordwise.service.intefaces.ITarjetaService;
import com.poo.wordwise.util.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TarjetaServiceImpl implements ITarjetaService {

    private ITarjetaRepository tajetaRepository;
    private IEstadoTarjetaRepository estadoTarjetaRepository;
    private CloudinaryService cloudinaryService;

    @Override
    public Page<TarjetaDTO> findAllByIdCategoria(Long idCategoria, Pageable pageable) {
        Page<Tarjeta> tarjetas = this.tajetaRepository.findAllByIdCategoria(idCategoria, pageable);
        return new PageImpl<>(tarjetas.getContent().stream().map(TarjetaMapper.INSTANCE::toDto).toList(),  pageable, tarjetas.getTotalElements());
    }

    @Override
    @Transactional
    public TarjetaDTO createTarjeta(TarjetaDTO tarjetaDTO, MultipartFile imagen) {
        //Se valida la existencia de la tarjeta
        boolean existeTarjeta = this.tajetaRepository.existsByPalabraAndIdCategoria(tarjetaDTO.getPalabra(), tarjetaDTO.getIdCategoria());
        if (existeTarjeta) throw new WordWiseValidationException("Ya existe la palabra para esta categoria");

        //Se asigna el estado POR REPASAR
        EstadoTarjeta estadoRepasar = getEstadoTarjeta("REP");
        Tarjeta tarjeta = TarjetaMapper.INSTANCE.toEntity(tarjetaDTO);

        if (imagen != null &&  !imagen.isEmpty()) {
            String url = subirImagen(imagen);
            tarjeta.setImagen(url);
        }

        tarjeta.setIdEstado(estadoRepasar.getId());
        tarjeta.setEstadoTarjeta(estadoRepasar);
        tarjeta.setFechaCreacion(LocalDateTime.now());
        return TarjetaMapper.INSTANCE.toDto(this.tajetaRepository.save(tarjeta));
    }

    private String subirImagen(MultipartFile imagen) {
        String url = null;
        if(imagen != null && !imagen.isEmpty()){
            try {
                url = this.cloudinaryService.uploadImage(imagen, "tarjetas");
            } catch (IOException e) {
                throw new WordWiseDataNotFoundException("Error al subir imagen : " + e.getMessage());
            }
        }
        return url;
    }

    @Override
    public Page<TarjetaDTO> findAllByIdCategoriaAndIdEstado(Long idCategoria, String estado, String query, Pageable pageable) {
        //Se busca el estado
        EstadoTarjeta estadoTarjeta = estado != null ? this.estadoTarjetaRepository.findBySigla(estado.trim()) : null;
        Page<Tarjeta> tarjetas = this.tajetaRepository.findAllByIdCategoriaAndIdEstado(idCategoria, estadoTarjeta != null ? estadoTarjeta.getId() : null, cleanValue(query), pageable);
        return new PageImpl<>(tarjetas.getContent().stream().map(TarjetaMapper.INSTANCE::toDto).toList(),  pageable, tarjetas.getTotalElements());
    }

    @Override
    public Page<TarjetaDTO> findAllFavoritesByIdCategoria(Long idCategoria, String query, Pageable pageable) {
        Page<Tarjeta> favoritos = this.tajetaRepository.findAllFavoritesByIdCategoria(idCategoria, cleanValue(query), pageable);
        return new PageImpl<>(favoritos.getContent().stream().map(TarjetaMapper.INSTANCE::toDto).toList(), pageable, favoritos.getTotalElements());
    }

    @Override
    @Transactional
    public TarjetaDTO updateEstadoTarjeta(Long idTarjeta, String estado) {
        //Buscamos el estado a asociar
        EstadoTarjeta estadoTarjeta = getEstadoTarjeta(estado);

        //Se busca la tarjeta
        Tarjeta tarjeta = getTargetaById(idTarjeta);
        tarjeta.setIdEstado(estadoTarjeta.getId());
        tarjeta.setEstadoTarjeta(estadoTarjeta);

        return TarjetaMapper.INSTANCE.toDto(this.tajetaRepository.save(tarjeta));
    }

    @Override
    @Transactional
    public TarjetaDTO deleteTarjeta(Long idTarjeta) {
        //Se busca la targeta a eliminar
        Tarjeta tarjeta = getTargetaById(idTarjeta);

        if(tarjeta.getImagen() != null){
            //Se elimina la imagen
            try {
                this.cloudinaryService.deleteImage(tarjeta.getImagen());
            } catch (IOException e) {
                throw new WordWiseValidationException(e.getMessage());
            }
            tarjeta.setImagen(null);
        }

        this.tajetaRepository.delete(tarjeta);

        return TarjetaMapper.INSTANCE.toDto(tarjeta);
    }

    @Override
    @Transactional
    public TarjetaDTO updateTarjeta(TarjetaDTO tarjetaDTO, MultipartFile imagen) {
        //Se busca la tarjeta a actualizar
        Tarjeta tarjeta = getTargetaById(tarjetaDTO.getId());

        tarjeta.setPalabra(tarjetaDTO.getPalabra());
        tarjeta.setTraduccion(tarjetaDTO.getTraduccion());
        tarjeta.setEsFavorita(tarjetaDTO.isEsFavorita());

        if(tarjeta.getImagen() != null ){
            try {
                this.cloudinaryService.deleteImage(tarjeta.getImagen());
            } catch (IOException e) {
                throw new WordWiseValidationException(e.getMessage());
            }
            tarjeta.setImagen(null);
        }

        if(imagen != null && !imagen.isEmpty()){
            String url = subirImagen(imagen);
            tarjeta.setImagen(url);
        }

        return TarjetaMapper.INSTANCE.toDto(this.tajetaRepository.save(tarjeta));
    }

    @Override
    public TarjetaDTO findTarjetaById(Long idTarjeta) {
        Tarjeta tarjeta = getTargetaById(idTarjeta);
        return TarjetaMapper.INSTANCE.toDto(tarjeta);
    }

    private Tarjeta getTargetaById(Long idTarjeta) {
        Optional<Tarjeta> tarjeta = this.tajetaRepository.findById(idTarjeta);
        if (tarjeta.isEmpty()) throw new WordWiseDataNotFoundException("Tarjeta no encontrada");
        return tarjeta.get();
    }

    private EstadoTarjeta getEstadoTarjeta(String siglaEstado){
        //Buscamos el estado de favoritos
        EstadoTarjeta estado = this.estadoTarjetaRepository.findBySigla(siglaEstado);
        if(estado == null) throw new WordWiseDataNotFoundException("No se encontro el estado");
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
