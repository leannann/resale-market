package ru.skypro.homework.exception;

/**
 * Исключение, выбрасываемое при попытке получить доступ
 * к объявлению, которое не существует в системе.
 * <p>
 * Используется при запросах к объявлениям по ID,
 * когда запись отсутствует в базе данных.
 */
public class AdNotFoundException extends RuntimeException {

    /**
     * Создаёт исключение с указанным сообщением.
     *
     * @param message текст ошибки
     */
    public AdNotFoundException(String message) {
        super(message);
    }
}
