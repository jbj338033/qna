package com.qna.domain.answer.domain.repository;

import com.qna.domain.answer.domain.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuestionIdOrderByCreatedAtAsc(Long questionId);

    Optional<Answer> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT COUNT(a) FROM Answer a WHERE a.question.id = :questionId")
    Long countByQuestionId(@Param("questionId") Long questionId);

    @Query("SELECT a FROM Answer a WHERE a.question.id = :questionId AND a.isAccepted = true")
    Optional<Answer> findAcceptedAnswerByQuestionId(@Param("questionId") Long questionId);
}
