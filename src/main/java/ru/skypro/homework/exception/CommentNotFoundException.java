package ru.skypro.homework.exception;

/**
 * Исключение, выбрасываемое в случаях, когда запрошенный комментарий
 * не найден в базе данных.
 * <p>
 * Используется при обращении к комментариям по ID или попытке изменения/удаления
 * несуществующего комментария.
 */
public class CommentNotFoundException extends RuntimeException {

    /**
     * Создаёт исключение с указанным сообщением.
     *
     * @param message описание ошибки
     */
    public CommentNotFoundException(String message) {
        super(message);
    }
}

