package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Ad}.
 * <p>
 * Расширяет {@link JpaRepository}, предоставляя стандартные CRUD-операции
 * и дополнительные методы поиска объявлений по автору и ключевому слову.
 */
public interface AdRepository extends JpaRepository<Ad, Integer> {

    /**
     * Возвращает список объявлений, созданных указанным пользователем.
     *
     * @param author объект пользователя — автора объявлений
     * @return список объявлений данного автора
     */
    List<Ad> findByAuthor(User author);

    /**
     * Выполняет поиск объявлений по части названия (регистронезависимо).
     *
     * @param keyword ключевое слово, которое должно входить в название объявления
     * @return список подходящих объявлений
     */
    List<Ad> findByTitleContainingIgnoreCase(String keyword);
}

