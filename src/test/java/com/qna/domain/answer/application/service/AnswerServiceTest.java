package com.qna.domain.answer.application.service;

import com.qna.domain.answer.application.mapper.AnswerMapper;
import com.qna.domain.answer.domain.entity.Answer;
import com.qna.domain.answer.domain.error.AnswerError;
import com.qna.domain.answer.domain.repository.AnswerRepository;
import com.qna.domain.answer.presentation.request.CreateAnswerRequest;
import com.qna.domain.answer.presentation.request.UpdateAnswerRequest;
import com.qna.domain.answer.presentation.response.AnswerResponse;
import com.qna.domain.question.domain.entity.Question;
import com.qna.domain.question.domain.repository.QuestionRepository;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

    @InjectMocks
    private AnswerServiceImpl answerService;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnswerMapper answerMapper;

    @Mock
    private SecurityUtil securityUtil;

    private User questionOwner;
    private User answerOwner;
    private Question question;
    private Answer answer;
    private AnswerResponse answerResponse;

    @BeforeEach
    void setUp() {
        questionOwner = User.builder()
                .email("question@example.com")
                .password("password")
                .nickname("질문작성자")
                .build();
        ReflectionTestUtils.setField(questionOwner, "id", 1L);

        answerOwner = User.builder()
                .email("answer@example.com")
                .password("password")
                .nickname("답변작성자")
                .build();
        ReflectionTestUtils.setField(answerOwner, "id", 2L);

        question = Question.builder()
                .title("테스트 질문")
                .content("테스트 내용")
                .user(questionOwner)
                .build();
        ReflectionTestUtils.setField(question, "id", 1L);
        ReflectionTestUtils.setField(question, "hasAcceptedAnswer", false);

        answer = Answer.builder()
                .content("테스트 답변입니다.")
                .question(question)
                .user(answerOwner)
                .build();
        ReflectionTestUtils.setField(answer, "id", 1L);
        ReflectionTestUtils.setField(answer, "isAccepted", false);

        answerResponse = AnswerResponse.builder()
                .id(1L)
                .content("테스트 답변입니다.")
                .isAccepted(false)
                .author(AnswerResponse.AuthorInfo.builder()
                        .id(2L)
                        .nickname("답변작성자")
                        .build())
                .questionId(1L)
                .build();
    }

    @Test
    @DisplayName("답변 작성 성공")
    void createAnswer_Success() {
        CreateAnswerRequest createRequest = new CreateAnswerRequest("테스트 답변입니다.");

        given(securityUtil.getCurrentUserId()).willReturn(2L);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(answerOwner));
        given(questionRepository.findById(anyLong())).willReturn(Optional.of(question));
        given(answerRepository.save(any(Answer.class))).willReturn(answer);
        given(answerMapper.toResponse(any(Answer.class))).willReturn(answerResponse);

        AnswerResponse response = answerService.createAnswer(1L, createRequest);

        assertThat(response.getContent()).isEqualTo("테스트 답변입니다.");
        assertThat(response.getAuthor().getNickname()).isEqualTo("답변작성자");
    }

    @Test
    @DisplayName("답변 수정 성공")
    void updateAnswer_Success() {
        UpdateAnswerRequest updateRequest = new UpdateAnswerRequest("수정된 답변입니다.");
        AnswerResponse updatedResponse = AnswerResponse.builder()
                .id(1L)
                .content("수정된 답변입니다.")
                .isAccepted(false)
                .build();

        given(securityUtil.getCurrentUserId()).willReturn(2L);
        given(answerRepository.findById(anyLong())).willReturn(Optional.of(answer));
        given(answerMapper.toResponse(any(Answer.class))).willReturn(updatedResponse);

        AnswerResponse response = answerService.updateAnswer(1L, updateRequest);

        assertThat(response.getContent()).isEqualTo("수정된 답변입니다.");
    }

    @Test
    @DisplayName("답변 수정 실패 - 권한 없음")
    void updateAnswer_Fail_Unauthorized() {
        UpdateAnswerRequest updateRequest = new UpdateAnswerRequest("수정된 답변입니다.");

        given(securityUtil.getCurrentUserId()).willReturn(1L);
        given(answerRepository.findById(anyLong())).willReturn(Optional.of(answer));

        assertThatThrownBy(() -> answerService.updateAnswer(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> {
                    BusinessException ex = (BusinessException) e;
                    assertThat(ex.getError()).isEqualTo(AnswerError.UNAUTHORIZED_ACCESS);
                });
    }

    @Test
    @DisplayName("답변 채택 성공")
    void acceptAnswer_Success() {
        AnswerResponse acceptedResponse = AnswerResponse.builder()
                .id(1L)
                .content("테스트 답변입니다.")
                .isAccepted(true)
                .build();

        given(securityUtil.getCurrentUserId()).willReturn(1L);
        given(answerRepository.findById(anyLong())).willReturn(Optional.of(answer));
        given(answerMapper.toResponse(any(Answer.class))).willReturn(acceptedResponse);

        AnswerResponse response = answerService.acceptAnswer(1L);

        assertThat(response.getIsAccepted()).isTrue();
    }

    @Test
    @DisplayName("답변 채택 실패 - 질문 작성자가 아님")
    void acceptAnswer_Fail_NotQuestionOwner() {
        given(securityUtil.getCurrentUserId()).willReturn(2L);
        given(answerRepository.findById(anyLong())).willReturn(Optional.of(answer));

        assertThatThrownBy(() -> answerService.acceptAnswer(1L))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> {
                    BusinessException ex = (BusinessException) e;
                    assertThat(ex.getError()).isEqualTo(AnswerError.NOT_QUESTION_OWNER);
                });
    }

    @Test
    @DisplayName("답변 채택 실패 - 이미 채택됨")
    void acceptAnswer_Fail_AlreadyAccepted() {
        ReflectionTestUtils.setField(answer, "isAccepted", true);

        given(securityUtil.getCurrentUserId()).willReturn(1L);
        given(answerRepository.findById(anyLong())).willReturn(Optional.of(answer));

        assertThatThrownBy(() -> answerService.acceptAnswer(1L))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> {
                    BusinessException ex = (BusinessException) e;
                    assertThat(ex.getError()).isEqualTo(AnswerError.ALREADY_ACCEPTED);
                });
    }
}
