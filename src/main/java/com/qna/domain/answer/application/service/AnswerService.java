package com.qna.domain.answer.application.service;

import com.qna.domain.answer.presentation.request.CreateAnswerRequest;
import com.qna.domain.answer.presentation.request.UpdateAnswerRequest;
import com.qna.domain.answer.presentation.response.AnswerResponse;

public interface AnswerService {
    AnswerResponse createAnswer(Long questionId, CreateAnswerRequest request);
    AnswerResponse updateAnswer(Long answerId, UpdateAnswerRequest request);
    void deleteAnswer(Long answerId);
    AnswerResponse acceptAnswer(Long answerId);
}
