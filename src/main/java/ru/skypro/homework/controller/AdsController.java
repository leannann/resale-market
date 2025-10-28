package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.util.Collections;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
        log.info("GET /ads");
        AdsDto body = new AdsDto();
        body.setCount(0);
        body.setResults(Collections.emptyList());
        return ResponseEntity.ok(body);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> addAd(@RequestPart("properties") CreateOrUpdateAdDto properties,
                                    @RequestPart("image") MultipartFile image) {
        log.info("POST /ads (multipart) title={}, image={}", properties != null ? properties.getTitle() : null,
                image != null ? image.getOriginalFilename() : null);

        AdDto ad = new AdDto(); // заглушка
        ad.setId(0);
        ad.setTitle(properties != null ? properties.getTitle() : null);
        ad.setPrice(properties != null ? properties.getPrice() : null);
        return ResponseEntity.status(HttpStatus.CREATED).body(ad);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDto> getAds(@PathVariable Integer id) {
        log.info("GET /ads/{}", id);
        return ResponseEntity.ok(new ExtendedAdDto()); // пустой объект
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Integer id) {
        log.info("DELETE /ads/{}", id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdDto> updateAds(@PathVariable Integer id,
                                        @RequestBody CreateOrUpdateAdDto request) {
        log.info("PATCH /ads/{} body={}", id, request);
        return ResponseEntity.ok(new AdDto()); // пустой объект
    }

    @GetMapping("/me")
    public ResponseEntity<AdsDto> getAdsMe() {
        log.info("GET /ads/me");
        AdsDto body = new AdsDto();
        body.setCount(0);
        body.setResults(Collections.emptyList());
        return ResponseEntity.ok(body);
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(@PathVariable Integer id,
                                              @RequestPart("image") MultipartFile image) {
        log.info("PATCH /ads/{}/image file={}", id,
                image != null ? image.getOriginalFilename() : null);
        return ResponseEntity.ok(new byte[0]);
    }
}