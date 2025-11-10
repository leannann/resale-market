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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.util.Collections;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Объявления", description = "CRUD-операции и работа с изображениями объявлений")
public class AdsController {

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
        log.info("GET /ads");
        AdsDto body = new AdsDto();
        body.setCount(0);
        body.setResults(Collections.emptyList());
        return ResponseEntity.ok(body);
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
            @Parameter(description = "Параметры объявления (JSON)") @RequestPart("properties") CreateOrUpdateAdDto properties,
            @Parameter(description = "Изображение объявления (файл)") @RequestPart("image") MultipartFile image
    ) {
        log.info("POST /ads (multipart) title={}, image={}", properties != null ? properties.getTitle() : null,
                image != null ? image.getOriginalFilename() : null);

        AdDto ad = new AdDto();
        ad.setId(0);
        ad.setTitle(properties != null ? properties.getTitle() : null);
        ad.setPrice(properties != null ? properties.getPrice() : null);
        return ResponseEntity.status(HttpStatus.CREATED).body(ad);
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
        log.info("GET /ads/{}", id);
        return ResponseEntity.ok(new ExtendedAdDto());
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
            @Parameter(description = "ID объявления") @PathVariable Integer id) {
        log.info("DELETE /ads/{}", id);
        return ResponseEntity.noContent().build();
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
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено")
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<AdDto> updateAds(
            @Parameter(description = "ID объявления") @PathVariable Integer id,
            @org.springframework.web.bind.annotation.RequestBody CreateOrUpdateAdDto request) {
        log.info("PATCH /ads/{} body={}", id, request);
        return ResponseEntity.ok(new AdDto());
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
    public ResponseEntity<AdsDto> getAdsMe() {
        log.info("GET /ads/me");
        AdsDto body = new AdsDto();
        body.setCount(0);
        body.setResults(Collections.emptyList());
        return ResponseEntity.ok(body);
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
                    @ApiResponse(responseCode = "200", description = "Изображение успешно обновлено"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено")
            }
    )
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(
            @Parameter(description = "ID объявления") @PathVariable Integer id,
            @Parameter(description = "Новое изображение (файл)") @RequestPart("image") MultipartFile image
    ) {
        log.info("PATCH /ads/{}/image file={}", id,
                image != null ? image.getOriginalFilename() : null);
        return ResponseEntity.ok(new byte[0]);
    }
}
