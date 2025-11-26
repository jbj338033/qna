package com.qna.domain.tag.presentation.response;

import com.qna.domain.tag.domain.entity.Tag;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TagResponse {
    private Long id;
    private String name;
    private Long questionCount;

    public static TagResponse from(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public static TagResponse of(Tag tag, Long questionCount) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .questionCount(questionCount)
                .build();
    }
}
