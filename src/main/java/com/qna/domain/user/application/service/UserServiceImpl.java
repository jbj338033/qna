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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(UserError.DUPLICATE_EMAIL);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(UserError.INVALID_PASSWORD);
        }

        String token = jwtTokenProvider.createToken(user.getEmail());
        return LoginResponse.of(token, user.getId(), user.getEmail(), user.getNickname());
    }
}
