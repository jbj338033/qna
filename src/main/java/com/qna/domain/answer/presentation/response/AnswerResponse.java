package com.qna.domain.answer.presentation.response;

import com.qna.domain.answer.domain.entity.Answer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AnswerResponse {
    private Long id;
    private String content;
    private Boolean isAccepted;
    private AuthorInfo author;
    private Long questionId;
    private LocalDateTime createdAt;

    @Getter
    @Builder
    public static class AuthorInfo {
        private Long id;
        private String nickname;
    }

    public static AnswerResponse from(Answer answer) {
        return AnswerResponse.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .isAccepted(answer.getIsAccepted())
                .author(AuthorInfo.builder()
                        .id(answer.getUser().getId())
                        .nickname(answer.getUser().getNickname())
                        .build())
                .questionId(answer.getQuestion().getId())
                .createdAt(answer.getCreatedAt())
                .build();
    }
}
