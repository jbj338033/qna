package com.qna.domain.user.application.service;

import com.qna.domain.user.presentation.request.LoginRequest;
import com.qna.domain.user.presentation.request.SignupRequest;
import com.qna.domain.user.presentation.response.LoginResponse;
import com.qna.domain.user.presentation.response.UserResponse;

public interface UserService {
    UserResponse signup(SignupRequest request);
    LoginResponse login(LoginRequest request);
}
