package com.qna.domain.answer.presentation.controller;

import com.qna.domain.answer.application.service.AnswerService;
import com.qna.domain.answer.presentation.docs.AnswerDocs;
import com.qna.domain.answer.presentation.request.CreateAnswerRequest;
import com.qna.domain.answer.presentation.request.UpdateAnswerRequest;
import com.qna.domain.answer.presentation.response.AnswerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AnswerController implements AnswerDocs {

    private final AnswerService answerService;

    @PostMapping("/api/questions/{questionId}/answers")
    public ResponseEntity<AnswerResponse> createAnswer(
            @PathVariable Long questionId,
            @Valid @RequestBody CreateAnswerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(answerService.createAnswer(questionId, request));
    }

    @PutMapping("/api/answers/{answerId}")
    public ResponseEntity<AnswerResponse> updateAnswer(
            @PathVariable Long answerId,
            @Valid @RequestBody UpdateAnswerRequest request) {
        return ResponseEntity.ok(answerService.updateAnswer(answerId, request));
    }

    @DeleteMapping("/api/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long answerId) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/answers/{answerId}/accept")
    public ResponseEntity<AnswerResponse> acceptAnswer(@PathVariable Long answerId) {
        return ResponseEntity.ok(answerService.acceptAnswer(answerId));
    }
}
