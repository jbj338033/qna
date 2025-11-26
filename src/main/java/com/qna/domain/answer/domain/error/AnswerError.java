package com.qna.domain.answer.domain.error;

import com.qna.global.exception.BaseError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AnswerError implements BaseError {
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "답변을 찾을 수 없습니다"),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "권한이 없습니다"),
    NOT_QUESTION_OWNER(HttpStatus.FORBIDDEN, "질문 작성자만 답변을 채택할 수 있습니다"),
    ALREADY_ACCEPTED(HttpStatus.BAD_REQUEST, "이미 채택된 답변입니다");

    private final HttpStatus status;
    private final String message;
}
