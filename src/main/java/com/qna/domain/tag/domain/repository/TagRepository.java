package com.qna.domain.tag.domain.repository;

import com.qna.domain.tag.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query("SELECT t, COUNT(qt) as cnt FROM Tag t LEFT JOIN QuestionTag qt ON t.id = qt.tag.id GROUP BY t.id ORDER BY cnt DESC")
    List<Object[]> findAllWithQuestionCount();
}
