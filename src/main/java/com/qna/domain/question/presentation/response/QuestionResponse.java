package com.qna.domain.question.presentation.response;

import com.qna.domain.answer.presentation.response.AnswerResponse;
import com.qna.domain.question.domain.entity.Question;
import com.qna.domain.tag.presentation.response.TagResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class QuestionResponse {
    private Long id;
    private String title;
    private String content;
    private Long viewCount;
    private Boolean hasAcceptedAnswer;
    private AuthorInfo author;
    private List<TagResponse> tags;
    private List<AnswerResponse> answers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @Builder
    public static class AuthorInfo {
        private Long id;
        private String nickname;
    }

    public static QuestionResponse from(Question question, List<TagResponse> tags) {
        return QuestionResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .viewCount(question.getViewCount())
                .hasAcceptedAnswer(question.getHasAcceptedAnswer())
                .author(AuthorInfo.builder()
                        .id(question.getUser().getId())
                        .nickname(question.getUser().getNickname())
                        .build())
                .tags(tags)
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }

    public static QuestionResponse from(Question question, List<TagResponse> tags, List<AnswerResponse> answers) {
        return QuestionResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .viewCount(question.getViewCount())
                .hasAcceptedAnswer(question.getHasAcceptedAnswer())
                .author(AuthorInfo.builder()
                        .id(question.getUser().getId())
                        .nickname(question.getUser().getNickname())
                        .build())
                .tags(tags)
                .answers(answers)
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }
}
