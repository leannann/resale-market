package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.entity.Comment;

@Mapper(uses = {UserMapper.class, AdMapper.class}, componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "authorImage", source = "author.image")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "text", source = "text")
    CommentDto commentToCommentDto(Comment comment);

    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "id", source = "pk")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "text", source = "text")
    Comment commentDtoToComment(CommentDto commentDto);
}
