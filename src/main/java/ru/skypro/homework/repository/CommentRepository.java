package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.Comment;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Comment}.
 * <p>
 * Расширяет {@link JpaRepository}, предоставляя стандартные CRUD-операции,
 * а также дополнительные методы для получения комментариев по объявлению и автору.
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    /**
     * Возвращает список комментариев, принадлежащих указанному объявлению.
     *
     * @param adId идентификатор объявления
     * @return список комментариев, связанных с данным объявлением
     */
    List<Comment> findByAdId(Integer adId);

    /**
     * Возвращает список комментариев, созданных указанным пользователем.
     *
     * @param authorId идентификатор автора комментариев
     * @return список комментариев пользователя
     */
    List<Comment> findByAuthorId(Integer authorId);
}

