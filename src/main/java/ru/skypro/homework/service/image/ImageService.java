package ru.skypro.homework.service.image;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * Сервис для работы с изображениями (аватары пользователей и изображения объявлений).
 * <p>
 * Предоставляет функциональность для сохранения, загрузки, удаления изображений,
 * а также определения типа содержимого и расширения файлов.
 */
public interface ImageService {

    /**
     * Сохраняет изображение в файловой системе.
     *
     * @param image     файл изображения
     * @param subfolder подкаталог, в котором будет сохранён файл
     *                  (например, {@code "avatars"} или {@code "ads"})
     * @return путь к сохранённому изображению
     * @throws IOException если произошла ошибка при сохранении файла
     */
    String saveImage(MultipartFile image, String subfolder) throws IOException;

    /**
     * Загружает изображение из файловой системы.
     *
     * @param imagePath путь к файлу изображения
     * @return массив байт, представляющий содержимое изображения
     * @throws IOException если файл отсутствует или недоступен
     */
    byte[] loadImage(String imagePath) throws IOException;

    /**
     * Удаляет изображение по указанному пути.
     *
     * @param imagePath путь к файлу изображения
     * @throws IOException если произошла ошибка при удалении файла
     */
    void deleteImage(String imagePath) throws IOException;

    /**
     * Определяет тип содержимого (MIME type) исходя из расширения файла.
     *
     * @param imagePath путь к файлу изображения
     * @return тип содержимого файла
     */
    MediaType determineMediaType(String imagePath);

    /**
     * Определяет тип содержимого изображения по загруженному файлу.
     *
     * @param file файл изображения
     * @return тип содержимого файла
     */
    MediaType determineMediaType(MultipartFile file);

    /**
     * Определяет расширение файла по типу содержимого.
     *
     * @param mediaType MIME тип содержимого
     * @return расширение файла (без точки), например {@code "jpg"} или {@code "png"}
     */
    String getFileExtension(MediaType mediaType);
}

