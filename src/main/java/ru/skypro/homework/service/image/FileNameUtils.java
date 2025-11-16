package ru.skypro.homework.service.image;

/**
 * Утилитный класс для работы с именами файлов изображений.
 * <p>
 * Предоставляет методы для генерации уникальных имен файлов
 * и извлечения расширения из исходного имени файла.
 * <br>
 * Класс является финальным и содержит только статические методы.
 */
public final class FileNameUtils {

    /**
     * Приватный конструктор предотвращает создание экземпляров утилитного класса.
     */
    private FileNameUtils() {
    }

    /**
     * Генерирует уникальное имя файла на основе исходного имени.
     * <p>
     * В итоговое имя включаются:
     * <ul>
     *     <li>префикс {@code "img_"}</li>
     *     <li>текущее время в миллисекундах</li>
     *     <li>хэш исходного имени файла</li>
     *     <li>расширение файла</li>
     * </ul>
     *
     * @param originalFilename исходное имя файла
     * @return уникальное имя файла, подходящее для сохранения
     */
    public static String generateFileName(String originalFilename) {
        String fileExtension = getFileName(originalFilename);
        return "img_" + System.currentTimeMillis() + "_"
                + Math.abs(originalFilename.hashCode()) + fileExtension;
    }

    /**
     * Извлекает расширение файла из его имени.
     * <p>
     * Если имя файла отсутствует или не содержит расширения,
     * по умолчанию возвращается {@code ".jpg"}.
     *
     * @param filename имя файла
     * @return расширение файла, включая точку (например, {@code ".png"})
     */
    public static String getFileName(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
