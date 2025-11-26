package com.qna.domain.question.presentation.controller;

import com.qna.domain.question.application.service.QuestionService;
import com.qna.domain.question.presentation.docs.QuestionDocs;
import com.qna.domain.question.presentation.request.CreateQuestionRequest;
import com.qna.domain.question.presentation.request.UpdateQuestionRequest;
import com.qna.domain.question.presentation.response.QuestionListResponse;
import com.qna.domain.question.presentation.response.QuestionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController implements QuestionDocs {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@Valid @RequestBody CreateQuestionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(questionService.createQuestion(request));
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.getQuestion(questionId));
    }

    @GetMapping
    public ResponseEntity<Page<QuestionListResponse>> getQuestions(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false, defaultValue = "latest") String sort,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(questionService.getQuestions(keyword, tag, sort, pageable));
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @PathVariable Long questionId,
            @Valid @RequestBody UpdateQuestionRequest request) {
        return ResponseEntity.ok(questionService.updateQuestion(questionId, request));
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }
}
