package com.poo.wordwise.service.impl;

import com.poo.wordwise.dto.CategoriaDTO;
import com.poo.wordwise.mapper.CategoriaMapper;
import com.poo.wordwise.model.Categoria;
import com.poo.wordwise.repository.ICategoriaRepository;
import com.poo.wordwise.service.intefaces.ICategoriaService;
import com.poo.wordwise.util.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CategoriaServiceImpl implements ICategoriaService {

    private static final Logger log = LoggerFactory.getLogger(CategoriaServiceImpl.class);
    private ICategoriaRepository categoriaRepository;
    private CloudinaryService cloudinaryService;

    @Override
    public List<CategoriaDTO> findAllByIdUsuario(Long idUsuario) {
        return categoriaRepository.findAllByIdUsuario(idUsuario).stream().map(CategoriaMapper.INSTANCE::toDto).toList();
    }

    @Override
    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO, MultipartFile imagen){
        //Se valida la existencia de la categoria por nombre
        boolean existeCategoria = this.categoriaRepository.existsByNombreAndIdUsuario(categoriaDTO.getNombre().trim(), categoriaDTO.getIdUsuario());
        if(existeCategoria) throw new IllegalArgumentException("Ya existe una categoria con este nombre asociada al usuario");

        Categoria categoria = CategoriaMapper.INSTANCE.toEntity(categoriaDTO);

        String url = null;

        if(imagen != null && !imagen.isEmpty()){
            try {
                url = this.cloudinaryService.uploadImage(imagen, "categorias");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            categoria.setImagen(url);
        }

        return CategoriaMapper.INSTANCE.toDto(categoriaRepository.save(categoria));
    }

    @Autowired
    public void setCategoriaRepository(ICategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Autowired
    public void setCloudinaryService(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }
}
