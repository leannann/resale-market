package ru.skypro.homework.service.comment.impl;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.comment.CommentService;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AdRepository adRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, AdRepository adRepository, UserRepository userRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentDto addComment(Integer adId, CreateOrUpdateCommentDto createCommentDto, String username) {
        log.debug("Добавление комментария к объявлению ID: {} пользователем: {}", adId, username);

        // Проверка аутентификации пользователя
        if (username == null || username.trim().isEmpty()) {
            log.warn("Попытка добавления комментария неаутентифицированным пользователем");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не аутентифицирован");
        }

        try {
            Ad ad = adRepository.findById(adId)
                    .orElseThrow(() -> new AdNotFoundException("Объявление с ID " + adId + " не найдено"));

            User author = userRepository.findByEmail(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не найден"));

            if (createCommentDto.getText() == null || createCommentDto.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Текст комментария не может быть пустым");
            }

            Comment comment = new Comment();
            comment.setAd(ad);
            comment.setAuthor(author);
            comment.setText(createCommentDto.getText());
            comment.setCreatedAt(Instant.now().toEpochMilli());

            Comment savedComment = commentRepository.save(comment);
            log.info("Добавлен комментарий к объявлению ID: {}", adId);

            return commentMapper.commentToCommentDto(savedComment);
        } catch (AdNotFoundException e) {
            log.warn("Объявление не найдено: ID {}", adId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("Некорректные данные комментария: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public CommentDto updateComment(Integer commentId, CreateOrUpdateCommentDto updateCommentDto, String username) {
        log.debug("Обновление комментария ID: {} пользователем: {}", commentId, username);

        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));

            if (updateCommentDto.getText() == null || updateCommentDto.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Текст комментария не может быть пустым");
            }

            comment.setText(updateCommentDto.getText());
            Comment updatedComment = commentRepository.save(comment);
            log.info("Обновлен комментарий ID: {}", commentId);

            return commentMapper.commentToCommentDto(updatedComment);
        } catch (CommentNotFoundException e) {
            log.warn("Комментарий не найден: ID {}", commentId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("Некорректные данные комментария: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public void deleteComment(Integer commentId, String username) {
        log.debug("Удаление комментария ID: {} пользователем: {}", commentId, username);

        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));

            commentRepository.delete(comment);
            log.info("Удален комментарий ID: {}", commentId);
        } catch (CommentNotFoundException e) {
            log.warn("Комментарий не найден: ID {}", commentId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public List<CommentDto> getCommentsByAdId(Integer adId) {
        log.debug("Получение комментариев для объявления ID: {}", adId);

        try {
            // Проверяем существование объявления
            if (!adRepository.existsById(adId)) {
                throw new AdNotFoundException("Объявление с ID " + adId + " не найдено");
            }

            List<Comment> comments = commentRepository.findByAdId(adId);
            return comments.stream()
                    .map(commentMapper::commentToCommentDto)
                    .collect(Collectors.toList());
        } catch (AdNotFoundException e) {
            log.warn("Объявление не найдено: ID {}", adId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //вспомогательный метод для проверки авторизации
    @Override
    public boolean isCommentAuthor(Integer commentId, String userEmail) {
        return commentRepository.findById(commentId)
                .map(comment -> comment.getAuthor().getEmail().equals(userEmail))
                .orElse(false);
    }
}
