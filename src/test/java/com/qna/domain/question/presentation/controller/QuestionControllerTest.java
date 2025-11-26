package com.qna.domain.question.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qna.domain.question.application.service.QuestionService;
import com.qna.domain.question.domain.error.QuestionError;
import com.qna.domain.question.presentation.response.QuestionListResponse;
import com.qna.domain.question.presentation.response.QuestionResponse;
import com.qna.global.exception.BusinessException;
import com.qna.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private QuestionController questionController;

    @Mock
    private QuestionService questionService;

    private ObjectMapper objectMapper;
    private QuestionResponse questionResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(questionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();

        questionResponse = QuestionResponse.builder()
                .id(1L)
                .title("테스트 질문")
                .content("테스트 내용입니다.")
                .viewCount(0L)
                .hasAcceptedAnswer(false)
                .author(QuestionResponse.AuthorInfo.builder()
                        .id(1L)
                        .nickname("테스트유저")
                        .build())
                .tags(Collections.emptyList())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("질문 목록 조회 성공 - 200 OK")
    void getQuestions_Success() throws Exception {
        QuestionListResponse listResponse = QuestionListResponse.builder()
                .id(1L)
                .title("테스트 질문")
                .viewCount(10L)
                .hasAcceptedAnswer(false)
                .answerCount(2L)
                .author(QuestionListResponse.AuthorInfo.builder()
                        .id(1L)
                        .nickname("테스트유저")
                        .build())
                .tags(Collections.emptyList())
                .createdAt(LocalDateTime.now())
                .build();

        Page<QuestionListResponse> page = new PageImpl<>(List.of(listResponse), PageRequest.of(0, 20), 1);

        given(questionService.getQuestions(any(), any(), any(), any())).willReturn(page);

        mockMvc.perform(get("/api/questions")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("테스트 질문"));
    }

    @Test
    @DisplayName("질문 상세 조회 성공 - 200 OK")
    void getQuestion_Success() throws Exception {
        given(questionService.getQuestion(anyLong())).willReturn(questionResponse);

        mockMvc.perform(get("/api/questions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트 질문"))
                .andExpect(jsonPath("$.content").value("테스트 내용입니다."));
    }

    @Test
    @DisplayName("질문 상세 조회 실패 - 404 Not Found")
    void getQuestion_Fail_NotFound() throws Exception {
        given(questionService.getQuestion(anyLong()))
                .willThrow(new BusinessException(QuestionError.QUESTION_NOT_FOUND));

        mockMvc.perform(get("/api/questions/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("QUESTION_NOT_FOUND"));
    }
}
