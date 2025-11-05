package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.entity.Comment;

@Mapper(uses = {UserMapper.class, AdMapper.class}, componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorImageUrl", source = "author.image")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAtEpochMillis", source = "createdAtEpochMillis")
    @Mapping(target = "text", source = "text")
    CommentDto commentToCommentDto(Comment comment);

    @Mapping(target = "ad", ignore = true)      // ad задаётся отдельно
    @Mapping(target = "author", ignore = true)  // author задаётся отдельно
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAtEpochMillis", source = "createdAtEpochMillis")
    @Mapping(target = "text", source = "text")
    Comment commentDtoToComment(CommentDto commentDto);
}
