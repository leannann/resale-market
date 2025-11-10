package ru.skypro.homework.service.add.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mappers.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.add.AdService;

@Service
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;

    private final AdMapper adMapper;
    private final UserRepository userRepository;

    public AdServiceImpl(AdRepository adRepository, UserRepository userRepository, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.adMapper = adMapper;
    }

    @Override
    public boolean isOwner(Integer adId, String userEmail) {
        return adRepository.findById(adId)
                .map(ad -> ad.getAuthor().getEmail().equals(userEmail))
                .orElse(false);
    }

    @Override
    public List<AdDto> getAllAds() {
        return adRepository.findAll().stream()
                .map(adMapper::adToAdDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdDto getAdById(Integer id) {
        return adRepository.findById(id)
                .map(adMapper::adToAdDto)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
    }

    @Override
    public AdDto createAd(AdDto adDto) {
        Ad ad = adMapper.adDtoToAd(adDto);
        // Предположим, что автор уже существует
        User author = userRepository.findById(adDto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Автор не найден"));
        ad.setAuthor(author);
        Ad savedAd = adRepository.save(ad);
        return adMapper.adToAdDto(savedAd);
    }

    @Override
    public void deleteAd(Integer id) {
        adRepository.deleteById(id);
    }
}
