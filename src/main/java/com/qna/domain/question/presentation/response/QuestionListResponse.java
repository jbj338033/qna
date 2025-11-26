package com.qna.domain.question.presentation.response;

import com.qna.domain.question.domain.entity.Question;
import com.qna.domain.tag.presentation.response.TagResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class QuestionListResponse {
    private Long id;
    private String title;
    private Long viewCount;
    private Boolean hasAcceptedAnswer;
    private Long answerCount;
    private AuthorInfo author;
    private List<TagResponse> tags;
    private LocalDateTime createdAt;

    @Getter
    @Builder
    public static class AuthorInfo {
        private Long id;
        private String nickname;
    }

    public static QuestionListResponse from(Question question, List<TagResponse> tags, Long answerCount) {
        return QuestionListResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .viewCount(question.getViewCount())
                .hasAcceptedAnswer(question.getHasAcceptedAnswer())
                .answerCount(answerCount)
                .author(AuthorInfo.builder()
                        .id(question.getUser().getId())
                        .nickname(question.getUser().getNickname())
                        .build())
                .tags(tags)
                .createdAt(question.getCreatedAt())
                .build();
    }
}
