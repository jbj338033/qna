package com.qna.domain.answer.application.service;

import com.qna.domain.answer.application.mapper.AnswerMapper;
import com.qna.domain.answer.domain.entity.Answer;
import com.qna.domain.answer.domain.error.AnswerError;
import com.qna.domain.answer.domain.repository.AnswerRepository;
import com.qna.domain.answer.presentation.request.CreateAnswerRequest;
import com.qna.domain.answer.presentation.request.UpdateAnswerRequest;
import com.qna.domain.answer.presentation.response.AnswerResponse;
import com.qna.domain.question.domain.entity.Question;
import com.qna.domain.question.domain.error.QuestionError;
import com.qna.domain.question.domain.repository.QuestionRepository;
import com.qna.domain.user.domain.entity.User;
import com.qna.domain.user.domain.error.UserError;
import com.qna.domain.user.domain.repository.UserRepository;
import com.qna.global.exception.BusinessException;
import com.qna.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerMapper answerMapper;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public AnswerResponse createAnswer(Long questionId, CreateAnswerRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(QuestionError.QUESTION_NOT_FOUND));

        Answer answer = Answer.builder()
                .content(request.getContent())
                .question(question)
                .user(user)
                .build();

        Answer savedAnswer = answerRepository.save(answer);
        return answerMapper.toResponse(savedAnswer);
    }

    @Override
    @Transactional
    public AnswerResponse updateAnswer(Long answerId, UpdateAnswerRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.ANSWER_NOT_FOUND));

        if (!answer.isOwner(userId)) {
            throw new BusinessException(AnswerError.UNAUTHORIZED_ACCESS);
        }

        answer.update(request.getContent());
        return answerMapper.toResponse(answer);
    }

    @Override
    @Transactional
    public void deleteAnswer(Long answerId) {
        Long userId = securityUtil.getCurrentUserId();
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.ANSWER_NOT_FOUND));

        if (!answer.isOwner(userId)) {
            throw new BusinessException(AnswerError.UNAUTHORIZED_ACCESS);
        }

        answerRepository.delete(answer);
    }

    @Override
    @Transactional
    public AnswerResponse acceptAnswer(Long answerId) {
        Long userId = securityUtil.getCurrentUserId();
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(AnswerError.ANSWER_NOT_FOUND));

        if (!answer.isQuestionOwner(userId)) {
            throw new BusinessException(AnswerError.NOT_QUESTION_OWNER);
        }

        answer.accept();
        return answerMapper.toResponse(answer);
    }
}
