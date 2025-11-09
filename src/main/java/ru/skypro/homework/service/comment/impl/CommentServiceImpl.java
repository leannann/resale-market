package ru.skypro.homework.service.comment.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.comment.CommentService;

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
    public List<CommentDto> getCommentsByAdId(Integer adId) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
        return commentRepository.findById(ad).stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(CommentDto commentDto) {
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        // Устанавливаем связанный Ad и User
        Ad ad = adRepository.findById(commentDto.getAdId())
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
        User author = userRepository.findById(commentDto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Автор не найден"));
        comment.setAd(ad);
        comment.setAuthor(author);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.commentToCommentDto(savedComment);
    }
}
