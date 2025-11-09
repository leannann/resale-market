package ru.skypro.homework.service.comment;

import java.util.List;

import ru.skypro.homework.dto.CommentDto;


public interface CommentService {
    List<CommentDto> getCommentsByAdId(Integer adId);

    CommentDto addComment(CommentDto commentDto);

}
