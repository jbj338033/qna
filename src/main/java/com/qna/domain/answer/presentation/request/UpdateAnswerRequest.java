package com.qna.domain.answer.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAnswerRequest {

    @NotBlank(message = "내용은 필수입니다")
    private String content;
}
