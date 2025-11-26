package com.qna.domain.answer.application.mapper;

import com.qna.domain.answer.domain.entity.Answer;
import com.qna.domain.answer.presentation.response.AnswerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    @Mapping(target = "author.id", source = "user.id")
    @Mapping(target = "author.nickname", source = "user.nickname")
    @Mapping(target = "questionId", source = "question.id")
    AnswerResponse toResponse(Answer answer);
}
