package ru.skypro.homework.service.add.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AdAccessDeniedException;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.mappers.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.add.AdService;
import ru.skypro.homework.service.image.ImageService;

@Slf4j
@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserRepository userRepository;
    private final ImageService imageService;

    public AdServiceImpl(AdRepository adRepository, UserRepository userRepository, AdMapper adMapper, ImageService imageService) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.adMapper = adMapper;
        this.imageService = imageService;
    }

    @Override
    public AdsDto getAllAds() {
        log.info("Получение списка всех объявлений");
        List<Ad> ads = adRepository.findAll();

        AdsDto adsDto = new AdsDto();
        adsDto.setCount(ads.size());
        adsDto.setResults(ads.stream()
                .map(adMapper::adToAdDto)
                .collect(Collectors.toList()));

        log.info("Найдено {} объявлений", adsDto.getCount());
        return adsDto;
    }

    @Override
    public AdDto createAd(CreateOrUpdateAdDto createAdDto, MultipartFile image, String username) throws IOException {
        log.info("Создание нового объявления пользователем: {}", username);

        // Находим автора
        User author = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.error("Пользователь {} не найден", username);
                    return new RuntimeException("Пользователь не найден");
                });

        if (createAdDto.getTitle() == null || createAdDto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Заголовок объявления не может быть пустым");
        }
        if (createAdDto.getPrice() == null || createAdDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Цена должна быть положительной");
        }
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Изображение обязательно");
        }

        // Сохраняем изображение
        String imagePath = imageService.saveImage(image, "ads");

        // Создаем объявление
        Ad ad = new Ad();
        ad.setAuthor(author);
        ad.setTitle(createAdDto.getTitle());
        ad.setPrice(createAdDto.getPrice());
        ad.setDescription(createAdDto.getDescription() != null ? createAdDto.getDescription() : "");
        ad.setImageUrl(imagePath);

        Ad savedAd = adRepository.save(ad);
        log.info("Объявление успешно создано: ID={}, заголовок='{}'", savedAd.getId(), savedAd.getTitle());

        return adMapper.adToAdDto(savedAd);
    }

    @Override
    public ExtendedAdDto getExtendedAdById(Integer id) {
        log.info("Получение расширенной информации об объявлении ID: {}", id);

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Объявление с ID {} не найдено", id);
                    return new AdNotFoundException("Объявление не найдено");
                });

        User author = ad.getAuthor();

        ExtendedAdDto extendedAd = new ExtendedAdDto();
        extendedAd.setPk(ad.getId());
        extendedAd.setTitle(ad.getTitle());
        extendedAd.setPrice(ad.getPrice());
        extendedAd.setDescription(ad.getDescription());
        extendedAd.setImage(ad.getImageUrl());
        extendedAd.setEmail(author.getEmail());
        extendedAd.setPhone(author.getPhone());
        extendedAd.setAuthorFirstName(author.getFirstName());
        extendedAd.setAuthorLastName(author.getLastName());
        log.info("Расширенная информация об объявлении ID={} найдена", id);

        return extendedAd;
    }

    @Override
    public void deleteAd(Integer id, String username) {
        log.info("Удаление объявления ID: {} пользователем: {}", id, username);

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено"));

        // Проверяем права: владелец ИЛИ админ
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!ad.getAuthor().getEmail().equals(username) && user.getRole() != Role.ADMIN) {
            log.warn("Попытка удаления чужого объявления: пользователь={}, объявление={}", username, id);
            throw new AdAccessDeniedException("Нет прав для удаления объявления");
        }

        adRepository.delete(ad);
        log.info("Объявление с ID {} успешно удалено", id);
    }

    @Override
    public AdDto updateAd(Integer id, CreateOrUpdateAdDto updateDto, String username) {
        log.info("Обновление объявления ID: {} пользователем: {}", id, username);

        Ad existingAd = adRepository.findById(id)
                .orElseThrow(() ->  new AdNotFoundException("Объявление не найдено"));

        // Получаем пользователя чтобы проверить роль
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        boolean isOwner = isOwner(id, username);
        boolean isAdmin = user.getRole() == Role.ADMIN;

        // Разрешаем обновление если пользователь ВЛАДЕЛЕЦ или АДМИН
        if (!isOwner && !isAdmin) {
            log.warn("Попытка обновления чужого объявления: пользователь={}, объявление={}", username, id);
            throw new AdAccessDeniedException("Нет прав для обновления объявления");
        }

        // Обновляем поля
        if (updateDto.getTitle() != null) {
            existingAd.setTitle(updateDto.getTitle());
        }
        if (updateDto.getPrice() != null) {
            existingAd.setPrice(updateDto.getPrice());
        }
        if (updateDto.getDescription() != null) {
            existingAd.setDescription(updateDto.getDescription());
        }

        Ad updatedAd = adRepository.save(existingAd);
        log.info("Объявление ID={} успешно обновлено", id);

        return adMapper.adToAdDto(updatedAd);
    }

    @Override
    public AdsDto getMyAds(String username) {
        log.info("Получение объявлений пользователя: {}", username);

        User author = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<Ad> userAds = adRepository.findByAuthor(author);

        AdsDto adsDto = new AdsDto();
        adsDto.setCount(userAds.size());
        adsDto.setResults(userAds.stream()
                .map(adMapper::adToAdDto)
                .collect(Collectors.toList()));

        log.info("Найдено {} объявлений пользователя {}", adsDto.getCount(), username);
        return adsDto;
    }

    @Override
    public void updateAdImage(Integer id, MultipartFile image, String username) throws IOException {
        log.info("Обновление изображения объявления ID: {} пользователем: {}", id, username);

        Ad existingAd = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено"));

        // Получаем пользователя чтобы проверить роль
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        boolean isOwner = isOwner(id, username);
        boolean isAdmin = user.getRole() == Role.ADMIN;

        // Разрешаем обновление изображения если пользователь ВЛАДЕЛЕЦ или АДМИН
        if (!isOwner && !isAdmin) {
            log.warn("Попытка обновления изображения чужого объявления: пользователь={}, объявление={}", username, id);
            throw new AdAccessDeniedException("Нет прав для обновления объявления");
        }

        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Изображение обязательно");
        }

        // Сохраняем изображение
        // Фронт отправляет файл → сервис сохраняет на диск → возвращает путь
        String newImagePath = imageService.saveImage(image, "ads");
        existingAd.setImageUrl(newImagePath);
        adRepository.save(existingAd);

        log.info("Изображение обновлено для объявления ID: {}", id);
    }

    @Override
    public String getAdImagePath(Integer id) {
        return adRepository.findById(id)
                .map(Ad::getImageUrl)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
    }


    @Override
    public boolean isOwner(Integer adId, String userEmail) {
        log.debug("Проверка прав доступа: объявление ID={}, пользователь={}", adId, userEmail);
        boolean isOwner = adRepository.findById(adId)
                .map(ad -> {
                    boolean result = ad.getAuthor().getEmail().equals(userEmail);
                    log.debug("Результат проверки прав: {}", result);
                    return result;
                })
                .orElse(false);

        if (!isOwner) {
            log.warn("Отказано в доступе: пользователь {} не является владельцем объявления {}",
                    userEmail, adId);
        }
        return isOwner;
    }
}
