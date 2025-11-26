package com.qna.domain.question.domain.entity;

import com.qna.domain.tag.domain.entity.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question_tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Builder
    public QuestionTag(Question question, Tag tag) {
        this.question = question;
        this.tag = tag;
    }
}
