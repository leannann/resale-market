package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import ru.skypro.homework.service.add.AdService;
import ru.skypro.homework.service.image.ImageService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Объявления", description = "CRUD-операции и работа с изображениями объявлений")
public class AdsController {

    private final AdService adService;
    private final ImageService imageService;

    @Operation(
            summary = "Получение всех объявлений",
            description = "Возвращает список всех объявлений с количеством и краткой информацией.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное получение списка объявлений",
                            content = @Content(schema = @Schema(implementation = AdsDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
        log.debug("GET /ads - Получение всех объявлений");

        AdsDto ads = adService.getAllAds();

        return ResponseEntity.ok(ads);
    }

    @Operation(
            summary = "Создание нового объявления",
            description = "Добавляет новое объявление. Принимает multipart-данные: JSON-часть с параметрами и изображение.",
            requestBody = @RequestBody(
                    description = "JSON-данные объявления и файл изображения",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Объявление успешно создано",
                            content = @Content(schema = @Schema(implementation = AdDto.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AdDto> addAd(
            @Parameter(description = "Параметры объявления")
            @RequestPart("properties") CreateOrUpdateAdDto properties, // ← Принимаем сразу DTO!
            @Parameter(description = "Изображение объявления")
            @RequestPart("image") MultipartFile image,
            Authentication authentication) {

        log.info("=== СОЗДАНИЕ ОБЪЯВЛЕНИЯ ===");
        log.info("Properties: {}", properties);
        log.info("Image: {} (size: {})", image.getOriginalFilename(), image.getSize());

        try {
            AdDto createdAd = adService.createAd(properties, image, authentication.getName());
            log.info("Объявление создано успешно: {}", createdAd);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);

        } catch (Exception e) {
            log.error("Ошибка при создании объявления: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    @Operation(
            summary = "Получение объявления по ID",
            description = "Возвращает расширенную информацию об объявлении.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Объявление найдено",
                            content = @Content(schema = @Schema(implementation = ExtendedAdDto.class))),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDto> getAds(
            @Parameter(description = "ID объявления") @PathVariable Integer id) {
        log.debug("GET /ads/{} - получение объявления", id);

        try {
            ExtendedAdDto extendedAd = adService.getExtendedAdById(id);
            return ResponseEntity.ok(extendedAd);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Удаление объявления",
            description = "Удаляет объявление по ID. Доступно только автору или администратору.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Объявление успешно удалено"),
                    @ApiResponse(responseCode = "403", description = "Нет прав для удаления"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @adService.isOwner(#id, authentication.name)")
    public ResponseEntity<Void> removeAd(
            @Parameter(description = "ID объявления") @PathVariable Integer id,
            Authentication authentication) {
        log.debug("DELETE /ads/{} - удаление объявления пользователем: {}", id, authentication.getName());

        try {
            adService.deleteAd(id, authentication.getName());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("не найдено")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("Нет прав")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Обновление объявления",
            description = "Изменяет данные существующего объявления по ID.",
            requestBody = @RequestBody(
                    description = "Новые параметры объявления",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateOrUpdateAdDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Объявление обновлено",
                            content = @Content(schema = @Schema(implementation = AdDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено")
            }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @adService.isOwner(#id, authentication.name)")
    public ResponseEntity<AdDto> updateAds(
            @Parameter(description = "ID объявления") @PathVariable Integer id,
            @org.springframework.web.bind.annotation.RequestBody CreateOrUpdateAdDto request,
            Authentication authentication) {
        log.debug("PATCH /ads/{} - обновление объявления пользователем: {}", id, authentication.getName());

        try {
            AdDto updatedAd = adService.updateAd(id, request, authentication.getName());
            return ResponseEntity.ok(updatedAd);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("не найдено")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("Нет прав")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Получение объявлений текущего пользователя",
            description = "Возвращает список объявлений, созданных авторизованным пользователем.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<AdsDto> getAdsMe(Authentication authentication) {
        log.debug("GET /ads/me - получение объявлений пользователя: {}", authentication.getName());

        AdsDto userAds = adService.getMyAds(authentication.getName());

        return ResponseEntity.ok(userAds);
    }

    @Operation(
            summary = "Обновление изображения объявления",
            description = "Заменяет изображение объявления по ID.",
            requestBody = @RequestBody(
                    description = "Новое изображение объявления",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Изображение успешно обновлено",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String[].class,
                                            example = "[\"/images/ads/img_1234567890.jpg\"]"))),
                    @ApiResponse(responseCode = "400", description = "Некорректный файл изображения"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено")
            }
    )
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or @adService.isOwner(#id, authentication.name)")
    public ResponseEntity<String[]> updateImage(
            @Parameter(description = "ID объявления") @PathVariable Integer id,
            @Parameter(description = "Новое изображение") @RequestPart("image") MultipartFile image,
            Authentication authentication) {
        log.debug("PATCH /ads/{}/image - обновление изображения", id);

        try {
            // Обновляем изображение
            adService.updateAdImage(id, image, authentication.getName());

            // Получаем путь к обновленному изображению
            String imagePath = adService.getAdImagePath(id);
            log.info("Изображение успешно обновлено для объявления ID: {}", id);

            return ResponseEntity.ok(new String[]{imagePath});
        } catch (IOException e) {
            log.error("Ошибка при обновлении изображения: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("не найдено")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("Нет прав")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            log.error("Неожиданная ошибка: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
