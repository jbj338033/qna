package com.qna.domain.tag.presentation.docs;

import com.qna.domain.tag.presentation.response.TagResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Tag", description = "태그 API")
public interface TagDocs {

    @Operation(summary = "전체 태그 조회", description = "모든 태그와 각 태그의 질문 수를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<TagResponse>> getAllTags();
}
