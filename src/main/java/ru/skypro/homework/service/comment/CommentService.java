package ru.skypro.homework.service.comment;

import java.util.List;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;


public interface CommentService {
    List<CommentDto> getCommentsByAdId(Integer adId);

    CommentDto addComment(Integer adId, CreateOrUpdateCommentDto createCommentDto, String username);

    CommentDto updateComment(Integer commentId, CreateOrUpdateCommentDto updateCommentDto, String username);

    void deleteComment(Integer commentId, String username);

    //вспомогательный метод для проверки авторизации
    boolean isCommentAuthor(Integer commentId, String userEmail);
}
