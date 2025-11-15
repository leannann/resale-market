package ru.skypro.homework.service.image;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;

public interface ImageService {
    String saveImage(MultipartFile image, String subfolder) throws IOException;

    byte[] loadImage(String imagePath) throws IOException;

    void deleteImage(String imagePath) throws IOException;

    MediaType determineMediaType(String imagePath);

    MediaType determineMediaType(MultipartFile file);

    String getFileExtension(MediaType mediaType);
}
