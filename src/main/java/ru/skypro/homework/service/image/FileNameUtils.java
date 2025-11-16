package ru.skypro.homework.service.image;

// Утилитный класс
public final class FileNameUtils {

    private FileNameUtils() {

    }

    public static String generateFileName(String originalFilename) {
        String fileExtension = getFileName(originalFilename);
        return "img_" + System.currentTimeMillis() + "_" +
                Math.abs(originalFilename.hashCode()) + fileExtension;
    }

    public static String getFileName(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
