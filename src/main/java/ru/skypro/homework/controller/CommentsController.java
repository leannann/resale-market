package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

import java.util.Collections;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class CommentsController {

    @GetMapping("/ads/{id}/comments")
    public ResponseEntity<CommentsDto> getComments(@PathVariable("id") Integer adId) {
        log.info("GET /ads/{}/comments", adId);
        CommentsDto body = new CommentsDto();
        body.setCount(0);
        body.setResults(Collections.emptyList());
        return ResponseEntity.ok(body);
    }

    @PostMapping("/ads/{id}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable("id") Integer adId,
                                                 @RequestBody CreateOrUpdateCommentDto request) {
        log.info("POST /ads/{}/comments body={}", adId, request);
        return ResponseEntity.ok(new CommentDto()); // по OAS 200 OK + Comment
    }

    @DeleteMapping("/ads/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer adId,
                                              @PathVariable Integer commentId) {
        log.info("DELETE /ads/{}/comments/{}", adId, commentId);
        return ResponseEntity.ok().build(); // по OAS 200 OK
    }

    @PatchMapping("/ads/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Integer adId,
                                                 @PathVariable Integer commentId,
                                                 @RequestBody CreateOrUpdateCommentDto request) {
        log.info("PATCH /ads/{}/comments/{} body={}", adId, commentId, request);
        return ResponseEntity.ok(new CommentDto());
    }
}