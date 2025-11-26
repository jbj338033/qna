package com.qna.domain.answer.presentation.docs;

import com.qna.domain.answer.presentation.request.CreateAnswerRequest;
import com.qna.domain.answer.presentation.request.UpdateAnswerRequest;
import com.qna.domain.answer.presentation.response.AnswerResponse;
import com.qna.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Answer", description = "답변 API")
public interface AnswerDocs {

    @Operation(summary = "답변 작성", description = "질문에 답변을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "답변 작성 성공"),
            @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AnswerResponse> createAnswer(
            @Parameter(description = "질문 ID") Long questionId,
            CreateAnswerRequest request);

    @Operation(summary = "답변 수정", description = "자신의 답변을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "답변을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AnswerResponse> updateAnswer(
            @Parameter(description = "답변 ID") Long answerId,
            UpdateAnswerRequest request);

    @Operation(summary = "답변 삭제", description = "자신의 답변을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> deleteAnswer(@Parameter(description = "답변 ID") Long answerId);

    @Operation(summary = "답변 채택", description = "질문 작성자가 답변을 채택합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채택 성공"),
            @ApiResponse(responseCode = "400", description = "이미 채택된 답변",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "질문 작성자가 아님",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AnswerResponse> acceptAnswer(@Parameter(description = "답변 ID") Long answerId);
}
