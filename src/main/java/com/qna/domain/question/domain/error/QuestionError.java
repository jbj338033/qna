package com.qna.domain.question.domain.error;

import com.qna.global.exception.BaseError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum QuestionError implements BaseError {
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "질문을 찾을 수 없습니다"),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "권한이 없습니다");

    private final HttpStatus status;
    private final String message;
}
