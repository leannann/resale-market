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
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.service.add.AdService;
import ru.skypro.homework.service.image.ImageService;

import java.io.IOException;

/**
 * REST-контроллер для работы с объявлениями.
 * <p>
 * Предоставляет операции:
 * <ul>
 *     <li>получение всех объявлений;</li>
 *     <li>создание нового объявления;</li>
 *     <li>просмотр объявления по ID;</li>
 *     <li>обновление и удаление объявления;</li>
 *     <li>получение объявлений авторизованного пользователя;</li>
 *     <li>обновление изображения объявления.</li>
 * </ul>
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Объявления", description = "CRUD-операции и работа с изображениями объявлений")
public class AdsController {

    private final AdService adService;
    private final ImageService imageService; // пока не используется, но может понадобиться в будущем

    /**
     * Возвращает список всех объявлений.
     *
     * @return DTO со списком объявлений и их количеством
     */
    @Operation(
            summary = "Получение всех объявлений",
            description = "Возвращает список всех объявлений с количеством и краткой информацией.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение списка объявлений",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
        log.debug("GET /ads — получение всех объявлений");

        AdsDto ads = adService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    /**
     * Создаёт новое объявление с изображением.
     * <p>
     * Принимает multipart-запрос: JSON-часть с параметрами объявления и файл изображения.
     * Доступно только для пользователей с ролью USER.
     *
     * @param properties    поля объявления (заголовок, цена, описание)
     * @param image         изображение объявления
     * @param authentication данные текущего авторизованного пользователя
     * @return созданное объявление
     */
    @Operation(
            summary = "Создание нового объявления",
            description = "Добавляет новое объявление. Принимает multipart-данные: JSON-часть с параметрами и изображение.",
            requestBody = @RequestBody(
                    description = "JSON-данные объявления и файл изображения",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Объявление успешно создано",
                            content = @Content(schema = @Schema(implementation = AdDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AdDto> addAd(
            @Parameter(description = "Параметры объявления")
            @RequestPart("properties") CreateOrUpdateAdDto properties,
            @Parameter(description = "Изображение объявления")
            @RequestPart("image") MultipartFile image,
            Authentication authentication) {

        log.info("POST /ads — создание объявления пользователем {}", authentication.getName());
        log.debug("Параметры: {}, файл: {} (size={})",
                properties, image.getOriginalFilename(), image.getSize());

        try {
            AdDto createdAd = adService.createAd(properties, image, authentication.getName());
            log.info("Объявление успешно создано: {}", createdAd);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);
        } catch (Exception e) {
            log.error("Ошибка при создании объявления: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Возвращает расширенную информацию об объявлении по его ID.
     *
     * @param id ID объявления
     * @return расширенное описание объявления или 404, если не найдено
     */
    @Operation(
            summary = "Получение объявления по ID",
            description = "Возвращает расширенную информацию об объявлении.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объявление найдено",
                            content = @Content(schema = @Schema(implementation = ExtendedAdDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDto> getAds(
            @Parameter(description = "ID объявления") @PathVariable Integer id) {

        log.debug("GET /ads/{} — получение объявления", id);

        try {
            ExtendedAdDto extendedAd = adService.getExtendedAdById(id);
            return ResponseEntity.ok(extendedAd);
        } catch (RuntimeException e) {
            log.warn("Объявление с ID {} не найдено: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Удаляет объявление по ID.
     * <p>
     * Доступно только автору объявления или администратору.
     *
     * @param id             ID объявления
     * @param authentication данные текущего пользователя
     * @return статус 204 при успехе или соответствующая ошибка
     */
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeAd(
            @Parameter(description = "ID объявления") @PathVariable Integer id,
            Authentication authentication) {

        log.debug("DELETE /ads/{} — удалить объявление, пользователь={}", id, authentication.getName());

        try {
            adService.deleteAd(id, authentication.getName());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Ошибка при удалении объявления ID {}: {}", id, e.getMessage());
            if (e.getMessage().contains("не найдено")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("Нет прав")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Обновляет данные объявления по ID.
     * <p>
     * Доступно автору объявления или администратору.
     *
     * @param id             ID объявления
     * @param request        новые данные объявления
     * @param authentication данные текущего пользователя
     * @return обновлённое объявление
     */
    @Operation(
            summary = "Обновление объявления",
            description = "Изменяет данные существующего объявления по ID.",
            requestBody = @RequestBody(
                    description = "Новые параметры объявления",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateOrUpdateAdDto.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объявление обновлено",
                            content = @Content(schema = @Schema(implementation = AdDto.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "403", description = "Нет прав на обновление"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено")
            }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AdDto> updateAds(
            @Parameter(description = "ID объявления") @PathVariable Integer id,
            @org.springframework.web.bind.annotation.RequestBody CreateOrUpdateAdDto request,
            Authentication authentication) {

        log.debug("PATCH /ads/{} — обновление объявления пользователем {}", id, authentication.getName());

        try {
            AdDto updatedAd = adService.updateAd(id, request, authentication.getName());
            return ResponseEntity.ok(updatedAd);
        } catch (RuntimeException e) {
            log.warn("Ошибка при обновлении объявления ID {}: {}", id, e.getMessage());
            if (e.getMessage().contains("не найдено")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("Нет прав")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Возвращает список объявлений, созданных текущим авторизованным пользователем.
     *
     * @param authentication данные текущего пользователя
     * @return список объявлений пользователя
     */
    @Operation(
            summary = "Получение объявлений текущего пользователя",
            description = "Возвращает список объявлений, созданных авторизованным пользователем.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение списка",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<AdsDto> getAdsMe(Authentication authentication) {
        log.debug("GET /ads/me — получение объявлений пользователя {}", authentication.getName());

        AdsDto userAds = adService.getMyAds(authentication.getName());
        return ResponseEntity.ok(userAds);
    }

    /**
     * Обновляет изображение объявления по ID.
     * <p>
     * Доступно автору объявления или администратору.
     *
     * @param id             ID объявления
     * @param image          новое изображение
     * @param authentication текущий пользователь
     * @return массив с путём к обновлённому изображению
     */
    @Operation(
            summary = "Обновление изображения объявления",
            description = "Заменяет изображение объявления по ID.",
            requestBody = @RequestBody(
                    description = "Новое изображение объявления",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Изображение успешно обновлено",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = String[].class,
                                            example = "[\"/images/ads/img_1234567890.jpg\"]"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректный файл изображения"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "403", description = "Нет прав для обновления"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено")
            }
    )
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or @adService.isOwner(#id, authentication.name)")
    public ResponseEntity<String[]> updateImage(
            @Parameter(description = "ID объявления") @PathVariable Integer id,
            @Parameter(description = "Новое изображение") @RequestPart("image") MultipartFile image,
            Authentication authentication) {

        log.debug("PATCH /ads/{}/image — обновление изображения пользователем {}", id, authentication.getName());

        try {
            adService.updateAdImage(id, image, authentication.getName());
            String imagePath = adService.getAdImagePath(id);
            log.info("Изображение успешно обновлено для объявления ID {}", id);
            return ResponseEntity.ok(new String[]{imagePath});
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при обновлении изображения: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Ошибка при обновлении изображения объявления ID {}: {}", id, e.getMessage());
            if (e.getMessage().contains("не найдено")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("Нет прав")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }
}

