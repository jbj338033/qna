package com.qna.domain.user.presentation.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private UserInfo user;

    @Getter
    @Builder
    public static class UserInfo {
        private Long id;
        private String email;
        private String nickname;
    }

    public static LoginResponse of(String token, Long id, String email, String nickname) {
        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .user(UserInfo.builder()
                        .id(id)
                        .email(email)
                        .nickname(nickname)
                        .build())
                .build();
    }
}
