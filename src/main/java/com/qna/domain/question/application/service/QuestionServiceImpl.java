package com.qna.domain.question.application.service;

import com.qna.domain.answer.application.mapper.AnswerMapper;
import com.qna.domain.answer.domain.repository.AnswerRepository;
import com.qna.domain.answer.presentation.response.AnswerResponse;
import com.qna.domain.question.application.mapper.QuestionMapper;
import com.qna.domain.question.domain.entity.Question;
import com.qna.domain.question.domain.entity.QuestionTag;
import com.qna.domain.question.domain.error.QuestionError;
import com.qna.domain.question.domain.repository.QuestionRepository;
import com.qna.domain.question.domain.repository.QuestionTagRepository;
import com.qna.domain.question.presentation.request.CreateQuestionRequest;
import com.qna.domain.question.presentation.request.UpdateQuestionRequest;
import com.qna.domain.question.presentation.response.QuestionListResponse;
import com.qna.domain.question.presentation.response.QuestionResponse;
import com.qna.domain.tag.application.mapper.TagMapper;
import com.qna.domain.tag.domain.entity.Tag;
import com.qna.domain.tag.domain.repository.TagRepository;
import com.qna.domain.tag.presentation.response.TagResponse;
import com.qna.domain.user.domain.entity.User;
import com.qna.domain.user.domain.error.UserError;
import com.qna.domain.user.domain.repository.UserRepository;
import com.qna.global.exception.BusinessException;
import com.qna.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final QuestionTagRepository questionTagRepository;
    private final AnswerRepository answerRepository;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;
    private final TagMapper tagMapper;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public QuestionResponse createQuestion(CreateQuestionRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        Question question = Question.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        Question savedQuestion = questionRepository.save(question);

        List<TagResponse> tagResponses = new ArrayList<>();
        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            tagResponses = saveTags(savedQuestion, request.getTagNames());
        }

        return questionMapper.toResponse(savedQuestion, tagResponses);
    }

    @Override
    @Transactional
    public QuestionResponse getQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(QuestionError.QUESTION_NOT_FOUND));

        question.increaseViewCount();

        List<TagResponse> tags = getTagResponses(questionId);
        List<AnswerResponse> answers = answerRepository.findByQuestionIdOrderByCreatedAtAsc(questionId)
                .stream()
                .map(answerMapper::toResponse)
                .toList();

        return questionMapper.toResponse(question, tags, answers);
    }

    @Override
    public Page<QuestionListResponse> getQuestions(String keyword, String tag, String sort, Pageable pageable) {
        Page<Question> questions;

        if (keyword != null && !keyword.isEmpty()) {
            questions = questionRepository.searchQuestions(keyword, pageable);
        } else if (tag != null && !tag.isEmpty()) {
            questions = questionRepository.findByTagName(tag, pageable);
        } else if ("unanswered".equals(sort)) {
            questions = questionRepository.findUnansweredQuestions(pageable);
        } else if ("popular".equals(sort)) {
            questions = questionRepository.findPopularQuestions(pageable);
        } else {
            questions = questionRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        return questions.map(q -> {
            List<TagResponse> tags = getTagResponses(q.getId());
            Long answerCount = answerRepository.countByQuestionId(q.getId());
            return questionMapper.toListResponse(q, tags, answerCount);
        });
    }

    @Override
    @Transactional
    public QuestionResponse updateQuestion(Long questionId, UpdateQuestionRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(QuestionError.QUESTION_NOT_FOUND));

        if (!question.isOwner(userId)) {
            throw new BusinessException(QuestionError.UNAUTHORIZED_ACCESS);
        }

        question.update(request.getTitle(), request.getContent());

        questionTagRepository.deleteByQuestionId(questionId);
        List<TagResponse> tagResponses = new ArrayList<>();
        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            tagResponses = saveTags(question, request.getTagNames());
        }

        return questionMapper.toResponse(question, tagResponses);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long questionId) {
        Long userId = securityUtil.getCurrentUserId();
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(QuestionError.QUESTION_NOT_FOUND));

        if (!question.isOwner(userId)) {
            throw new BusinessException(QuestionError.UNAUTHORIZED_ACCESS);
        }

        questionTagRepository.deleteByQuestionId(questionId);
        questionRepository.delete(question);
    }

    private List<TagResponse> saveTags(Question question, List<String> tagNames) {
        List<TagResponse> tagResponses = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));

            questionTagRepository.save(QuestionTag.builder()
                    .question(question)
                    .tag(tag)
                    .build());

            tagResponses.add(tagMapper.toResponse(tag));
        }
        return tagResponses;
    }

    private List<TagResponse> getTagResponses(Long questionId) {
        return questionTagRepository.findByQuestionId(questionId).stream()
                .map(qt -> tagMapper.toResponse(qt.getTag()))
                .toList();
    }
}
