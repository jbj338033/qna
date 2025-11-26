package com.qna.domain.tag.application.service;

import com.qna.domain.tag.application.mapper.TagMapper;
import com.qna.domain.tag.domain.entity.Tag;
import com.qna.domain.tag.domain.repository.TagRepository;
import com.qna.domain.tag.presentation.response.TagResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public List<TagResponse> getAllTags() {
        return tagRepository.findAllWithQuestionCount().stream()
                .map(result -> {
                    Tag tag = (Tag) result[0];
                    Long count = (Long) result[1];
                    return tagMapper.toResponse(tag, count);
                })
                .toList();
    }
}
