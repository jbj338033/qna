package com.qna.domain.question.application.service;

import com.qna.domain.answer.application.mapper.AnswerMapper;
import com.qna.domain.answer.domain.repository.AnswerRepository;
import com.qna.domain.question.application.mapper.QuestionMapper;
import com.qna.domain.question.domain.entity.Question;
import com.qna.domain.question.domain.error.QuestionError;
import com.qna.domain.question.domain.repository.QuestionRepository;
import com.qna.domain.question.domain.repository.QuestionTagRepository;
import com.qna.domain.question.presentation.request.CreateQuestionRequest;
import com.qna.domain.question.presentation.request.UpdateQuestionRequest;
import com.qna.domain.question.presentation.response.QuestionResponse;
import com.qna.domain.tag.application.mapper.TagMapper;
import com.qna.domain.tag.domain.repository.TagRepository;
import com.qna.domain.user.domain.entity.User;
import com.qna.domain.user.domain.repository.UserRepository;
import com.qna.global.exception.BusinessException;
import com.qna.global.security.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @InjectMocks
    private QuestionServiceImpl questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private QuestionTagRepository questionTagRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionMapper questionMapper;

    @Mock
    private AnswerMapper answerMapper;

    @Mock
    private TagMapper tagMapper;

    @Mock
    private SecurityUtil securityUtil;

    private User user;
    private Question question;
    private CreateQuestionRequest createRequest;
    private UpdateQuestionRequest updateRequest;
    private QuestionResponse questionResponse;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@example.com")
                .password("password")
                .nickname("테스트유저")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        question = Question.builder()
                .title("테스트 질문")
                .content("테스트 내용입니다.")
                .user(user)
                .build();
        ReflectionTestUtils.setField(question, "id", 1L);
        ReflectionTestUtils.setField(question, "viewCount", 0L);

        createRequest = new CreateQuestionRequest("테스트 질문", "테스트 내용입니다.", new ArrayList<>());
        updateRequest = new UpdateQuestionRequest("수정된 질문", "수정된 내용입니다.", new ArrayList<>());

        questionResponse = QuestionResponse.builder()
                .id(1L)
                .title("테스트 질문")
                .content("테스트 내용입니다.")
                .author(QuestionResponse.AuthorInfo.builder()
                        .id(1L)
                        .nickname("테스트유저")
                        .build())
                .build();
    }

    @Test
    @DisplayName("질문 작성 성공")
    void createQuestion_Success() {
        given(securityUtil.getCurrentUserId()).willReturn(1L);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(questionRepository.save(any(Question.class))).willReturn(question);
        given(questionMapper.toResponse(any(Question.class), anyList())).willReturn(questionResponse);

        QuestionResponse response = questionService.createQuestion(createRequest);

        assertThat(response.getTitle()).isEqualTo("테스트 질문");
        assertThat(response.getAuthor().getNickname()).isEqualTo("테스트유저");
    }

    @Test
    @DisplayName("질문 수정 성공")
    void updateQuestion_Success() {
        QuestionResponse updatedResponse = QuestionResponse.builder()
                .id(1L)
                .title("수정된 질문")
                .content("수정된 내용입니다.")
                .build();

        given(securityUtil.getCurrentUserId()).willReturn(1L);
        given(questionRepository.findById(anyLong())).willReturn(Optional.of(question));
        doNothing().when(questionTagRepository).deleteByQuestionId(anyLong());
        given(questionMapper.toResponse(any(Question.class), anyList())).willReturn(updatedResponse);

        QuestionResponse response = questionService.updateQuestion(1L, updateRequest);

        assertThat(response.getTitle()).isEqualTo("수정된 질문");
        assertThat(response.getContent()).isEqualTo("수정된 내용입니다.");
    }

    @Test
    @DisplayName("질문 수정 실패 - 권한 없음")
    void updateQuestion_Fail_Unauthorized() {
        given(securityUtil.getCurrentUserId()).willReturn(2L);
        given(questionRepository.findById(anyLong())).willReturn(Optional.of(question));

        assertThatThrownBy(() -> questionService.updateQuestion(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> {
                    BusinessException ex = (BusinessException) e;
                    assertThat(ex.getError()).isEqualTo(QuestionError.UNAUTHORIZED_ACCESS);
                });
    }

    @Test
    @DisplayName("질문 삭제 성공")
    void deleteQuestion_Success() {
        given(securityUtil.getCurrentUserId()).willReturn(1L);
        given(questionRepository.findById(anyLong())).willReturn(Optional.of(question));
        doNothing().when(questionTagRepository).deleteByQuestionId(anyLong());
        doNothing().when(questionRepository).delete(any(Question.class));

        questionService.deleteQuestion(1L);

        verify(questionRepository, times(1)).delete(question);
    }

    @Test
    @DisplayName("질문 삭제 실패 - 권한 없음")
    void deleteQuestion_Fail_Unauthorized() {
        given(securityUtil.getCurrentUserId()).willReturn(2L);
        given(questionRepository.findById(anyLong())).willReturn(Optional.of(question));

        assertThatThrownBy(() -> questionService.deleteQuestion(1L))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> {
                    BusinessException ex = (BusinessException) e;
                    assertThat(ex.getError()).isEqualTo(QuestionError.UNAUTHORIZED_ACCESS);
                });
    }

    @Test
    @DisplayName("질문 조회수 증가 확인")
    void getQuestion_ViewCountIncrease() {
        given(questionRepository.findById(anyLong())).willReturn(Optional.of(question));
        given(questionTagRepository.findByQuestionId(anyLong())).willReturn(Collections.emptyList());
        given(answerRepository.findByQuestionIdOrderByCreatedAtAsc(anyLong())).willReturn(Collections.emptyList());
        given(questionMapper.toResponse(any(Question.class), anyList(), anyList())).willReturn(questionResponse);

        Long initialViewCount = question.getViewCount();
        questionService.getQuestion(1L);

        assertThat(question.getViewCount()).isEqualTo(initialViewCount + 1);
    }
}
