package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.User;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link User}.
 * <p>
 * Предоставляет стандартные CRUD-операции через {@link JpaRepository},
 * а также метод поиска пользователя по email.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Находит пользователя по его email.
     *
     * @param email email пользователя
     * @return {@link Optional} c найденным пользователем или пустой объект, если пользователь не найден
     */
    Optional<User> findByEmail(String email);
}

