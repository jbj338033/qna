package com.qna.domain.user.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qna.domain.user.application.service.UserService;
import com.qna.domain.user.domain.error.UserError;
import com.qna.domain.user.presentation.request.LoginRequest;
import com.qna.domain.user.presentation.request.SignupRequest;
import com.qna.domain.user.presentation.response.LoginResponse;
import com.qna.domain.user.presentation.response.UserResponse;
import com.qna.global.exception.BusinessException;
import com.qna.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("회원가입 성공 - 201 Created")
    void signup_Success() throws Exception {
        SignupRequest request = new SignupRequest("test@example.com", "password123!", "테스트유저");

        UserResponse response = UserResponse.builder()
                .id(1L)
                .email("test@example.com")
                .nickname("테스트유저")
                .createdAt(LocalDateTime.now())
                .build();

        given(userService.signup(any(SignupRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.nickname").value("테스트유저"));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 이메일 400 Bad Request")
    void signup_Fail_DuplicateEmail() throws Exception {
        SignupRequest request = new SignupRequest("test@example.com", "password123!", "테스트유저");

        given(userService.signup(any(SignupRequest.class)))
                .willThrow(new BusinessException(UserError.DUPLICATE_EMAIL));

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("DUPLICATE_EMAIL"));
    }

    @Test
    @DisplayName("로그인 성공 - 200 OK")
    void login_Success() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "password123!");
        LoginResponse response = LoginResponse.of("test-jwt-token", 1L, "test@example.com", "테스트유저");

        given(userService.login(any(LoginRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("test-jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호 401 Unauthorized")
    void login_Fail_InvalidPassword() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "wrongpassword");

        given(userService.login(any(LoginRequest.class)))
                .willThrow(new BusinessException(UserError.INVALID_PASSWORD));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("INVALID_PASSWORD"));
    }
}
