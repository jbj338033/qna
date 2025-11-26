package com.qna.domain.tag.application.service;

import com.qna.domain.tag.presentation.response.TagResponse;

import java.util.List;

public interface TagService {
    List<TagResponse> getAllTags();
}
