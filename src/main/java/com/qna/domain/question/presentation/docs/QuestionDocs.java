package com.qna.domain.question.presentation.docs;

import com.qna.domain.question.presentation.request.CreateQuestionRequest;
import com.qna.domain.question.presentation.request.UpdateQuestionRequest;
import com.qna.domain.question.presentation.response.QuestionListResponse;
import com.qna.domain.question.presentation.response.QuestionResponse;
import com.qna.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Question", description = "질문 API")
public interface QuestionDocs {

    @Operation(summary = "질문 작성", description = "새로운 질문을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "질문 작성 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<QuestionResponse> createQuestion(CreateQuestionRequest request);

    @Operation(summary = "질문 상세 조회", description = "질문 ID로 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<QuestionResponse> getQuestion(@Parameter(description = "질문 ID") Long questionId);

    @Operation(summary = "질문 목록 조회", description = "질문 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<Page<QuestionListResponse>> getQuestions(
            @Parameter(description = "검색 키워드") String keyword,
            @Parameter(description = "태그 필터") String tag,
            @Parameter(description = "정렬 (latest, popular, unanswered)") String sort,
            @Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "질문 수정", description = "자신의 질문을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<QuestionResponse> updateQuestion(
            @Parameter(description = "질문 ID") Long questionId,
            UpdateQuestionRequest request);

    @Operation(summary = "질문 삭제", description = "자신의 질문을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> deleteQuestion(@Parameter(description = "질문 ID") Long questionId);
}
