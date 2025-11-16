package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.entity.Comment;

/**
 * Маппер для преобразования между сущностью {@link Comment}
 * и DTO-комментария {@link CommentDto}.
 * <p>
 * Использует MapStruct для автоматической генерации реализации маппера.
 * Включает маппинг полей автора, объявления и служебных полей комментария.
 */
@Mapper(uses = {UserMapper.class, AdMapper.class}, componentModel = "spring")
public interface CommentMapper {

    /**
     * Преобразует сущность {@link Comment} в объект {@link CommentDto}.
     * <p>
     * Особенности маппинга:
     * <ul>
     *     <li>{@code author → author.id} — ID автора переносится в DTO;</li>
     *     <li>{@code author.image → authorImage} — путь к аватару автора;</li>
     *     <li>{@code author.firstName → authorFirstName} — имя автора;</li>
     *     <li>{@code id → pk} — ID комментария переносится в поле pk;</li>
     *     <li>{@code createdAt} и {@code text} копируются напрямую.</li>
     * </ul>
     *
     * @param comment сущность комментария
     * @return DTO комментария {@link CommentDto}
     */
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "authorImage", source = "author.image")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "text", source = "text")
    CommentDto commentToCommentDto(Comment comment);

    /**
     * Преобразует объект {@link CommentDto} в сущность {@link Comment}.
     * <p>
     * Особенности маппинга:
     * <ul>
     *     <li>{@code ad} — игнорируется, так как подставляется вручную в сервисе;</li>
     *     <li>{@code author} — игнорируется, назначается сервисом на основании авторизации;</li>
     *     <li>{@code pk → id} — ID комментария переносится в сущность;</li>
     *     <li>{@code createdAt} и {@code text} копируются напрямую.</li>
     * </ul>
     *
     * @param commentDto DTO комментария
     * @return сущность {@link Comment}
     */
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "id", source = "pk")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "text", source = "text")
    Comment commentDtoToComment(CommentDto commentDto);
}

