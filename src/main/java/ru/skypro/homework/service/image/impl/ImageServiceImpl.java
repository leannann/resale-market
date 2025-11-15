package ru.skypro.homework.service.image.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.image.FileNameUtils;
import ru.skypro.homework.service.image.ImageService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    @Value("${app.image.upload-dir:uploads/images/}")
    private String uploadDir;

    @Value("${app.image.base-url:http://localhost:8080}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir, "avatars"));
            Files.createDirectories(Paths.get(uploadDir, "ads"));
            log.info("Директории для изображений созданы");
        } catch (IOException e) {
            log.error("Ошибка создания директорий", e);
        }
    }

    @Override
    public String saveImage(MultipartFile image, String subfolder) throws IOException {
        log.debug("Сохранение изображения: {}", image.getOriginalFilename());

        // Валидация
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Файл изображения пустой");
        }

        // Проверка типа файла
        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Файл должен быть изображением");
        }

        // Генерация имени файла
        String fileName = FileNameUtils.generateFileName(image.getOriginalFilename());

        // Путь для сохранения
        String fullUploadDir = uploadDir + subfolder + "/";
        Path uploadPath = Paths.get(fullUploadDir);

        // Создаем директорию если не существует
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Сохраняем файл
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String imagePath = "/images/" + subfolder + "/" + fileName;
        String fullImageUrl = baseUrl + imagePath;
        log.info("Изображение сохранено: {} (URL: {})", filePath, fullImageUrl);

        return imagePath;
    }

    @Override
    public byte[] loadImage(String imagePath) throws IOException {
        log.debug("Загрузка изображения: {}", imagePath);

        // Убираем начальный слэш для создания полного пути
        String relativePath = imagePath.startsWith("/images/") ? imagePath.substring(8) : imagePath;
        Path filePath = Paths.get(uploadDir, relativePath);

        if (!Files.exists(filePath)) {
            log.warn("Изображение не найдено: {}", filePath);
            throw new IOException("Изображение не найдено");
        }

        return Files.readAllBytes(filePath);
    }

    @Override
    public void deleteImage(String imagePath) throws IOException {
        log.debug("Удаление изображения: {}", imagePath);

        String relativePath = imagePath.startsWith("/images/") ? imagePath.substring(8) : imagePath;
        Path filePath = Paths.get(uploadDir, relativePath);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.info("Изображение удалено: {}", filePath);
        } else {
            log.warn("Изображение для удаления не найдено: {}", filePath);
        }
    }

    @Override
    public MediaType determineMediaType(String imagePath) {
        if (imagePath == null) {
            return MediaType.IMAGE_JPEG;
        }

        String lowerPath = imagePath.toLowerCase();
        if (lowerPath.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (lowerPath.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else if (lowerPath.endsWith(".webp")) {
            return MediaType.parseMediaType("image/webp");
        } else if (lowerPath.endsWith(".bmp")) {
            return MediaType.parseMediaType("image/bmp");
        } else {
            return MediaType.IMAGE_JPEG; // по умолчанию
        }
    }

    /**
     * Определяет MediaType по MultipartFile
     */
    @Override
    public MediaType determineMediaType(MultipartFile file) {
        if (file.getContentType() != null && file.getContentType().startsWith("image/")) {
            return MediaType.parseMediaType(file.getContentType());
        }

        // Если Content-Type неопределен, определяем по имени файла
        return determineMediaType(file.getOriginalFilename());
    }

    /**
     * Определяет расширение файла по MediaType
     */
    @Override
    public String getFileExtension(MediaType mediaType) {
        if (mediaType.equals(MediaType.IMAGE_PNG)) {
            return ".png";
        } else if (mediaType.equals(MediaType.IMAGE_GIF)) {
            return ".gif";
        } else if (mediaType.toString().equals("image/webp")) {
            return ".webp";
        } else if (mediaType.toString().equals("image/bmp")) {
            return ".bmp";
        } else {
            return ".jpg";
        }
    }
}

