package ru.skypro.homework.service.comment;

import java.util.List;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

/**
 * Сервис для работы с комментариями к объявлениям.
 * <p>
 * Предоставляет функциональность по получению, добавлению,
 * обновлению и удалению комментариев, а также проверку прав автора.
 */
public interface CommentService {

    /**
     * Возвращает список комментариев, принадлежащих указанному объявлению.
     *
     * @param adId идентификатор объявления
     * @return список комментариев в виде {@link CommentDto}
     */
    List<CommentDto> getCommentsByAdId(Integer adId);

    /**
     * Добавляет новый комментарий к объявлению от имени указанного пользователя.
     *
     * @param adId              идентификатор объявления
     * @param createCommentDto  данные для создания комментария
     * @param username          email пользователя, создающего комментарий
     * @return созданный комментарий в виде {@link CommentDto}
     */
    CommentDto addComment(Integer adId,
                          CreateOrUpdateCommentDto createCommentDto,
                          String username);

    /**
     * Обновляет существующий комментарий.
     *
     * @param commentId         идентификатор комментария
     * @param updateCommentDto  новые данные комментария
     * @param username          email пользователя, выполняющего обновление
     * @return обновлённый комментарий
     */
    CommentDto updateComment(Integer commentId,
                             CreateOrUpdateCommentDto updateCommentDto,
                             String username);

    /**
     * Удаляет комментарий по его идентификатору.
     *
     * @param commentId идентификатор комментария
     * @param username  email пользователя, выполняющего удаление
     */
    void deleteComment(Integer commentId, String username);

    /**
     * Проверяет, является ли пользователь автором комментария.
     *
     * @param commentId идентификатор комментария
     * @param userEmail email пользователя
     * @return {@code true}, если пользователь является автором комментария
     */
    boolean isCommentAuthor(Integer commentId, String userEmail);
}

