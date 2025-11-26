package com.qna.domain.tag.application.mapper;

import com.qna.domain.tag.domain.entity.Tag;
import com.qna.domain.tag.presentation.response.TagResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(target = "questionCount", ignore = true)
    TagResponse toResponse(Tag tag);

    @Mapping(target = "questionCount", source = "questionCount")
    TagResponse toResponse(Tag tag, Long questionCount);
}
