package ru.skypro.homework.service.add;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;

import java.io.IOException;

/**
 * Сервис для управления объявлениями.
 * <p>
 * Определяет операции по созданию, обновлению, удалению и получению объявлений,
 * а также по работе с изображениями объявлений. Сервис используется
 * контроллерами для реализации REST API.
 */
public interface AdService {

    /**
     * Возвращает список всех объявлений в системе.
     *
     * @return объект {@link AdsDto}, содержащий список объявлений
     */
    AdsDto getAllAds();

    /**
     * Создаёт новое объявление с изображением.
     *
     * @param createAdDto данные нового объявления
     * @param image       изображение объявления
     * @param username    email автора объявления
     * @return созданное объявление в виде {@link AdDto}
     * @throws IOException если изображение не удалось сохранить
     */
    AdDto createAd(CreateOrUpdateAdDto createAdDto,
                   MultipartFile image,
                   String username) throws IOException;

    /**
     * Возвращает расширенную информацию по объявлению.
     *
     * @param id идентификатор объявления
     * @return объект {@link ExtendedAdDto} с подробными данными
     */
    ExtendedAdDto getExtendedAdById(Integer id);

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param id       идентификатор объявления
     * @param username email пользователя, выполняющего удаление
     */
    void deleteAd(Integer id, String username);

    /**
     * Обновляет данные объявления.
     *
     * @param id        идентификатор объявления
     * @param updateDto новые данные объявления
     * @param username  email пользователя, выполняющего обновление
     * @return обновленное объявление в виде {@link AdDto}
     */
    AdDto updateAd(Integer id,
                   CreateOrUpdateAdDto updateDto,
                   String username);

    /**
     * Возвращает список объявлений, созданных авторизованным пользователем.
     *
     * @param username email автора объявлений
     * @return список объявлений пользователя в виде {@link AdsDto}
     */
    AdsDto getMyAds(String username);

    /**
     * Обновляет изображение объявления.
     *
     * @param id        идентификатор объявления
     * @param image     новое изображение
     * @param username  email пользователя, выполняющего обновление
     * @throws IOException если изображение не удалось сохранить
     */
    void updateAdImage(Integer id,
                       MultipartFile image,
                       String username) throws IOException;

    /**
     * Проверяет, является ли пользователь владельцем объявления.
     *
     * @param adId      идентификатор объявления
     * @param userEmail email пользователя
     * @return {@code true}, если пользователь является автором объявления
     */
    boolean isOwner(Integer adId, String userEmail);

    /**
     * Возвращает путь к изображению объявления.
     *
     * @param id идентификатор объявления
     * @return путь к изображению
     */
    String getAdImagePath(Integer id);
}

