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
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AdAccessDeniedException;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.mappers.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.add.AdService;
import ru.skypro.homework.service.image.ImageService;

/**
 * Реализация сервиса {@link AdService} для управления объявлениями.
 * <p>
 * Сервис реализует операции:
 * <ul>
 *     <li>получение списка объявлений;</li>
 *     <li>создание нового объявления;</li>
 *     <li>обновление данных или изображения объявления;</li>
 *     <li>удаление объявления;</li>
 *     <li>проверка прав владельца.</li>
 * </ul>
 * <p>
 * Включает проверки аутентификации, авторизации и корректности входящих данных.
 */
@Slf4j
@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final CommentRepository commentRepository;

    public AdServiceImpl(AdRepository adRepository, UserRepository userRepository, AdMapper adMapper, ImageService imageService, CommentRepository commentRepository) {
    /**
     * Конструктор сервиса.
     *
     * @param adRepository  репозиторий объявлений
     * @param userRepository репозиторий пользователей
     * @param adMapper      маппер сущностей объявлений в DTO
     * @param imageService  сервис для работы с изображениями
     */
    public AdServiceImpl(AdRepository adRepository,
                         UserRepository userRepository,
                         AdMapper adMapper,
                         ImageService imageService) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.adMapper = adMapper;
        this.imageService = imageService;
        this.commentRepository = commentRepository;
    }

    /**
     * Возвращает список всех объявлений в системе.
     *
     * @return объект {@link AdsDto}, содержащий количество и список объявлений
     */
    @Override
    public AdsDto getAllAds() {
        log.info("Получение списка всех объявлений");

        List<Ad> ads = adRepository.findAll();

        AdsDto adsDto = new AdsDto();
        adsDto.setCount(ads.size());
        adsDto.setResults(
                ads.stream()
                        .map(adMapper::adToAdDto)
                        .collect(Collectors.toList())
        );

        log.info("Найдено {} объявлений", adsDto.getCount());
        return adsDto;
    }

    /**
     * Создаёт новое объявление.
     * <p>
     * Производит валидацию данных и сохраняет изображение через {@link ImageService}.
     *
     * @param createAdDto данные нового объявления
     * @param image       файл изображения
     * @param username    email автора объявления
     * @return созданное объявление в формате {@link AdDto}
     * @throws IOException если изображение не удалось сохранить
     * @throws IllegalArgumentException если данные объявления некорректны
     */
    @Override
    public AdDto createAd(CreateOrUpdateAdDto createAdDto, MultipartFile image, String username) throws IOException {
        log.info("Создание нового объявления пользователем: {}", username);

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

        String imagePath = imageService.saveImage(image, "ads");

        Ad ad = new Ad();
        ad.setAuthor(author);
        ad.setTitle(createAdDto.getTitle());
        ad.setPrice(createAdDto.getPrice());
        ad.setDescription(createAdDto.getDescription() != null ? createAdDto.getDescription() : "");
        ad.setImageUrl(imagePath);

        Ad savedAd = adRepository.save(ad);
        log.info("Объявление создано: ID={}", savedAd.getId());

        return adMapper.adToAdDto(savedAd);
    }

    /**
     * Возвращает расширенную информацию об объявлении.
     *
     * @param id идентификатор объявления
     * @return объект {@link ExtendedAdDto} с подробными данными
     * @throws AdNotFoundException если объявление не найдено
     */
    @Override
    public ExtendedAdDto getExtendedAdById(Integer id) {
        log.info("Получение расширенной информации об объявлении ID: {}", id);

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Объявление с ID {} не найдено", id);
                    return new AdNotFoundException("Объявление не найдено");
                });

        User author = ad.getAuthor();

        ExtendedAdDto dto = new ExtendedAdDto();
        dto.setPk(ad.getId());
        dto.setTitle(ad.getTitle());
        dto.setPrice(ad.getPrice());
        dto.setDescription(ad.getDescription());
        dto.setImage(ad.getImageUrl());
        dto.setEmail(author.getEmail());
        dto.setPhone(author.getPhone());
        dto.setAuthorFirstName(author.getFirstName());
        dto.setAuthorLastName(author.getLastName());

        return dto;
    }

    /**
     * Удаляет объявление.
     * <p>
     * Удаление разрешено только владельцу объявления или администратору.
     *
     * @param id       идентификатор объявления
     * @param username email пользователя, выполняющего удаление
     * @throws AdAccessDeniedException если нет прав на удаление
     */
    @Override
    public void deleteAd(Integer id, String username) {
        log.info("Удаление объявления ID: {} пользователем: {}", id, username);

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено"));

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!ad.getAuthor().getEmail().equals(username) && user.getRole() != Role.ADMIN) {
            log.warn("Попытка удаления чужого объявления пользователем {}", username);
            throw new AdAccessDeniedException("Нет прав для удаления объявления");
        }

        // сначала удаляем все комментарии этого объявления
        List<Comment> comments = commentRepository.findByAdId(id);
        if (!comments.isEmpty()) {
            log.debug("Удаление {} комментариев объявления ID: {}", comments.size(), id);
            commentRepository.deleteAll(comments);
        }

        adRepository.delete(ad);
        log.info("Объявление ID {} удалено", id);
    }

    /**
     * Обновляет данные объявления.
     * <p>
     * Разрешено владельцу или администратору.
     *
     * @param id        идентификатор объявления
     * @param updateDto новые данные объявления
     * @param username  email пользователя
     * @return обновленное объявление
     * @throws AdAccessDeniedException если нет прав на обновление
     */
    @Override
    public AdDto updateAd(Integer id, CreateOrUpdateAdDto updateDto, String username) {
        log.info("Обновление объявления ID: {} пользователем: {}", id, username);

        Ad existingAd = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено"));

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        boolean isOwner = isOwner(id, username);
        boolean isAdmin = user.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            log.warn("Нет прав на обновление объявления ID {}", id);
            throw new AdAccessDeniedException("Нет прав для обновления объявления");
        }

        if (updateDto.getTitle() != null) existingAd.setTitle(updateDto.getTitle());
        if (updateDto.getPrice() != null) existingAd.setPrice(updateDto.getPrice());
        if (updateDto.getDescription() != null) existingAd.setDescription(updateDto.getDescription());

        Ad updatedAd = adRepository.save(existingAd);

        return adMapper.adToAdDto(updatedAd);
    }

    /**
     * Получает объявления, созданные авторизованным пользователем.
     *
     * @param username email автора
     * @return список объявлений пользователя
     */
    @Override
    public AdsDto getMyAds(String username) {
        log.info("Получение объявлений пользователя {}", username);

        User author = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<Ad> ads = adRepository.findByAuthor(author);

        AdsDto dto = new AdsDto();
        dto.setCount(ads.size());
        dto.setResults(
                ads.stream()
                        .map(adMapper::adToAdDto)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    /**
     * Обновляет изображение объявления.
     *
     * @param id       идентификатор объявления
     * @param image    новое изображение
     * @param username email пользователя
     * @throws IOException если файл не удалось сохранить
     */
    @Override
    public void updateAdImage(Integer id, MultipartFile image, String username) throws IOException {
        log.info("Обновление изображения объявления ID {} пользователем {}", id, username);

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено"));

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        boolean isOwner = isOwner(id, username);
        boolean isAdmin = user.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new AdAccessDeniedException("Нет прав для обновления объявления");
        }

        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Изображение обязательно");
        }

        String newImagePath = imageService.saveImage(image, "ads");
        ad.setImageUrl(newImagePath);
        adRepository.save(ad);
    }

    /**
     * Возвращает путь к изображению объявления.
     *
     * @param id идентификатор объявления
     * @return путь к изображению
     */
    @Override
    public String getAdImagePath(Integer id) {
        return adRepository.findById(id)
                .map(Ad::getImageUrl)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
    }

    /**
     * Проверяет, является ли пользователь владельцем объявления.
     *
     * @param adId      идентификатор объявления
     * @param userEmail email пользователя
     * @return {@code true}, если пользователь владелец
     */
    @Override
    public boolean isOwner(Integer adId, String userEmail) {
        log.debug("Проверка прав владельца объявления ID {} для пользователя {}", adId, userEmail);

        boolean isOwner = adRepository.findById(adId)
                .map(ad -> ad.getAuthor().getEmail().equals(userEmail))
                .orElse(false);

        return isOwner;
    }
}

