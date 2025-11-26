package com.qna.domain.question.domain.repository;

import com.qna.domain.question.domain.entity.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {

    List<QuestionTag> findByQuestionId(Long questionId);

    void deleteByQuestionId(Long questionId);
}
