package com.qna.domain.question.application.service;

import com.qna.domain.question.presentation.request.CreateQuestionRequest;
import com.qna.domain.question.presentation.request.UpdateQuestionRequest;
import com.qna.domain.question.presentation.response.QuestionListResponse;
import com.qna.domain.question.presentation.response.QuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionService {
    QuestionResponse createQuestion(CreateQuestionRequest request);
    QuestionResponse getQuestion(Long questionId);
    Page<QuestionListResponse> getQuestions(String keyword, String tag, String sort, Pageable pageable);
    QuestionResponse updateQuestion(Long questionId, UpdateQuestionRequest request);
    void deleteQuestion(Long questionId);
}
