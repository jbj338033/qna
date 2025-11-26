package com.qna.domain.question.application.mapper;

import com.qna.domain.answer.presentation.response.AnswerResponse;
import com.qna.domain.question.domain.entity.Question;
import com.qna.domain.question.presentation.response.QuestionListResponse;
import com.qna.domain.question.presentation.response.QuestionResponse;
import com.qna.domain.tag.presentation.response.TagResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "author.id", source = "question.user.id")
    @Mapping(target = "author.nickname", source = "question.user.nickname")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "answers", source = "answers")
    QuestionResponse toResponse(Question question, List<TagResponse> tags, List<AnswerResponse> answers);

    @Mapping(target = "author.id", source = "question.user.id")
    @Mapping(target = "author.nickname", source = "question.user.nickname")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "answers", ignore = true)
    QuestionResponse toResponse(Question question, List<TagResponse> tags);

    @Mapping(target = "author.id", source = "question.user.id")
    @Mapping(target = "author.nickname", source = "question.user.nickname")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "answerCount", source = "answerCount")
    QuestionListResponse toListResponse(Question question, List<TagResponse> tags, Long answerCount);
}
