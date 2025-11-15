package ru.skypro.homework.service.add;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;

import java.io.IOException;

public interface AdService {
    AdsDto getAllAds();

    AdDto createAd(CreateOrUpdateAdDto createAdDto, MultipartFile image, String username) throws IOException;

    // GET /ads/{id} - Получение информации об объявлении
    ExtendedAdDto getExtendedAdById(Integer id);

    void deleteAd(Integer id, String username);

    // PATCH /ads/{id} - Обновление информации об объявлении
    AdDto updateAd(Integer id, CreateOrUpdateAdDto updateDto, String username);

    // GET /ads/me - Получение объявлений авторизованного пользователя
    AdsDto getMyAds(String username);

    // PATCH /ads/{id}/image - Обновление картинки объявления
    void updateAdImage(Integer id, MultipartFile image, String username) throws IOException;

    // Вспомогательные методы
    boolean isOwner(Integer adId, String userEmail);

    String getAdImagePath(Integer id);
}
