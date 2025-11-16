package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

/**
 * REST-контроллер, отвечающий за выдачу изображений,
 * загруженных пользователями и связанных с объявлениями.
 * <p>
 * Позволяет получить картинку по её относительному пути.
 * Поддерживает форматы JPEG, PNG, GIF и WebP.
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
@Tag(name = "Изображения", description = "Получение изображений, связанных с объявлениями и пользователями")
public class ImageController {

    private final ImageService imageService;

    /**
     * Возвращает изображение по указанному пути.
     * <p>
     * Пример запроса:
     * <ul>
     *     <li>/images/ads/img_123.jpg</li>
     *     <li>/images/avatars/user_456.png</li>
     * </ul>
     *
     * @param subfolder каталог (ads / avatars)
     * @param filename имя файла изображения
     * @return изображение в бинарном виде или 404, если файл отсутствует
     */
    @Operation(
            summary = "Получение изображения",
            description = "Возвращает изображение по указанному пути (/images/{subfolder}/{filename})"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Изображение успешно получено",
                    content = @Content(mediaType = "image/jpeg")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Изображение не найдено",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @GetMapping(
            value = "/{subfolder}/{filename:.+}",
            produces = {
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_GIF_VALUE,
                    "image/*"
            }
    )
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

