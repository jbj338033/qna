package com.qna.domain.question.domain.repository;

import com.qna.domain.question.domain.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT q FROM Question q WHERE q.title LIKE %:keyword% OR q.content LIKE %:keyword% ORDER BY q.createdAt DESC")
    Page<Question> searchQuestions(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT q FROM Question q JOIN QuestionTag qt ON q.id = qt.question.id WHERE qt.tag.name = :tagName ORDER BY q.createdAt DESC")
    Page<Question> findByTagName(@Param("tagName") String tagName, Pageable pageable);

    @Query("SELECT q FROM Question q WHERE q.hasAcceptedAnswer = false ORDER BY q.createdAt DESC")
    Page<Question> findUnansweredQuestions(Pageable pageable);

    @Query("SELECT q FROM Question q ORDER BY q.viewCount DESC")
    Page<Question> findPopularQuestions(Pageable pageable);

    Page<Question> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
