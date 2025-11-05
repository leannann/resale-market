package ru.skypro.homework.service.add;

import ru.skypro.homework.dto.AdDto;

import java.util.List;

public interface AdService {
    List<AdDto> getAllAds();

    AdDto getAdById(Integer id);

    AdDto createAd(AdDto adDto);

    void deleteAd(Integer id);
}
