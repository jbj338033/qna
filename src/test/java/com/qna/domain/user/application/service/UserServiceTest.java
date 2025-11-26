package com.qna.domain.user.application.service;

import com.qna.domain.user.application.mapper.UserMapper;
import com.qna.domain.user.domain.entity.User;
import com.qna.domain.user.domain.error.UserError;
import com.qna.domain.user.domain.repository.UserRepository;
import com.qna.domain.user.presentation.request.LoginRequest;
import com.qna.domain.user.presentation.request.SignupRequest;
import com.qna.domain.user.presentation.response.LoginResponse;
import com.qna.domain.user.presentation.response.UserResponse;
import com.qna.global.exception.BusinessException;
import com.qna.global.security.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtTokenProvider;

    @Mock
    private UserMapper userMapper;

    private User user;
    private SignupRequest signupRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .nickname("테스트유저")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        signupRequest = new SignupRequest("test@example.com", "password123!", "테스트유저");
        loginRequest = new LoginRequest("test@example.com", "password123!");
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_Success() {
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .email("test@example.com")
                .nickname("테스트유저")
                .build();

        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userMapper.toResponse(any(User.class))).willReturn(userResponse);

        UserResponse response = userService.signup(signupRequest);

        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getNickname()).isEqualTo("테스트유저");
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 이메일")
    void signup_Fail_DuplicateEmail() {
        given(userRepository.existsByEmail(anyString())).willReturn(true);

        assertThatThrownBy(() -> userService.signup(signupRequest))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> {
                    BusinessException ex = (BusinessException) e;
                    assertThat(ex.getError()).isEqualTo(UserError.DUPLICATE_EMAIL);
                });
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(jwtTokenProvider.createToken(anyString())).willReturn("testToken");

        LoginResponse response = userService.login(loginRequest);

        assertThat(response.getAccessToken()).isEqualTo("testToken");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getUser().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_Fail_InvalidPassword() {
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> {
                    BusinessException ex = (BusinessException) e;
                    assertThat(ex.getError()).isEqualTo(UserError.INVALID_PASSWORD);
                });
    }
}
