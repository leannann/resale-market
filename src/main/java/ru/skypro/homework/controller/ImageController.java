package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.image.ImageService;

import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    /**
     * Endpoint для получения изображений по полному пути
     * Пример: /images/ads/img_123.jpg, /images/avatars/user_456.png
     */
    @GetMapping(value = "/{subfolder}/{filename:.+}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_GIF_VALUE, "image/*"})
    public ResponseEntity<byte[]> getImage(
            @PathVariable String subfolder,
            @PathVariable String filename) {

        String imagePath = "/images/" + subfolder + "/" + filename;
        log.info("Запрос изображения: {}", imagePath);

        try {
            byte[] imageBytes = imageService.loadImage(imagePath);
            MediaType mediaType = imageService.determineMediaType(imagePath);

            log.info("Изображение успешно отправлено: {} (размер: {} байт)", imagePath, imageBytes.length);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(imageBytes);

        } catch (IOException e) {
            log.error("Изображение не найдено: {}", imagePath);
            return ResponseEntity.notFound().build();
        }
    }
}
