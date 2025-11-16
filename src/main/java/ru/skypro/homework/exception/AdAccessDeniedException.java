package ru.skypro.homework.exception;

/**
 * Исключение, выбрасываемое при попытке доступа к объявлению без достаточных прав.
 * <p>
 * Используется в случаях, когда пользователь пытается изменить или удалить объявление,
 * не являясь его владельцем или администратором.
 */
public class AdAccessDeniedException extends RuntimeException {

    /**
     * Создаёт исключение с указанным сообщением.
     *
     * @param message текст ошибки
     */
    public AdAccessDeniedException(String message) {
        super(message);
    }
}
