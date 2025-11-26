package com.qna.domain.answer.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qna.domain.answer.application.service.AnswerService;
import com.qna.domain.answer.domain.error.AnswerError;
import com.qna.domain.answer.presentation.response.AnswerResponse;
import com.qna.global.exception.BusinessException;
import com.qna.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AnswerControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AnswerController answerController;

    @Mock
    private AnswerService answerService;

    private ObjectMapper objectMapper;
    private AnswerResponse answerResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(answerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        answerResponse = AnswerResponse.builder()
                .id(1L)
                .content("테스트 답변입니다.")
                .isAccepted(false)
                .author(AnswerResponse.AuthorInfo.builder()
                        .id(1L)
                        .nickname("답변작성자")
                        .build())
                .questionId(1L)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("답변 채택 성공 - 200 OK")
    void acceptAnswer_Success() throws Exception {
        AnswerResponse acceptedResponse = AnswerResponse.builder()
                .id(1L)
                .content("테스트 답변입니다.")
                .isAccepted(true)
                .author(AnswerResponse.AuthorInfo.builder()
                        .id(1L)
                        .nickname("답변작성자")
                        .build())
                .questionId(1L)
                .createdAt(LocalDateTime.now())
                .build();

        given(answerService.acceptAnswer(anyLong())).willReturn(acceptedResponse);

        mockMvc.perform(post("/api/answers/1/accept"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAccepted").value(true));
    }

    @Test
    @DisplayName("답변 채택 실패 - 질문 작성자가 아님 403 Forbidden")
    void acceptAnswer_Fail_NotQuestionOwner() throws Exception {
        given(answerService.acceptAnswer(anyLong()))
                .willThrow(new BusinessException(AnswerError.NOT_QUESTION_OWNER));

        mockMvc.perform(post("/api/answers/1/accept"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("NOT_QUESTION_OWNER"));
    }

    @Test
    @DisplayName("답변 채택 실패 - 이미 채택됨 400 Bad Request")
    void acceptAnswer_Fail_AlreadyAccepted() throws Exception {
        given(answerService.acceptAnswer(anyLong()))
                .willThrow(new BusinessException(AnswerError.ALREADY_ACCEPTED));

        mockMvc.perform(post("/api/answers/1/accept"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("ALREADY_ACCEPTED"));
    }
}
