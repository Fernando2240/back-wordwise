package com.poo.wordwise.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class CloudinaryService {

    private Cloudinary cloudinary;

    /**
     * SUBIR imagen
     * @param file - archivo de imagen
     * @param folder - carpeta (ej: "categorias", "tarjetas")
     * @return URL de la imagen
     */
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        String publicId = folder + "/" + UUID.randomUUID();

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", publicId,
                        "folder", folder,
                        "resource_type", "auto",
                        "quality", "auto",
                        "fetch_format", "auto"
                )
        );

        return (String) uploadResult.get("secure_url");
    }

    /**
     * ELIMINAR imagen
     * @param imageUrl - URL completa de la imagen
     */
    public void deleteImage(String imageUrl) throws IOException {
        String publicId = extractPublicId(imageUrl);

        if (publicId != null) {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
    }

    /**
     * Método auxiliar para extraer public_id de la URL
     */
    private String extractPublicId(String imageUrl) {
        try {
            String[] parts = imageUrl.split("/upload/");
            if (parts.length > 1) {
                String pathWithVersion = parts[1];
                String path = pathWithVersion.contains("/")
                        ? pathWithVersion.substring(pathWithVersion.indexOf("/") + 1)
                        : pathWithVersion;
                int lastDot = path.lastIndexOf(".");
                return lastDot > 0 ? path.substring(0, lastDot) : path;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Autowired
    public void setCloudinary(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
}