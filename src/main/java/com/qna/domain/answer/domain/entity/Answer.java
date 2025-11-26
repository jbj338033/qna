package com.qna.domain.answer.domain.entity;

import com.qna.domain.answer.domain.error.AnswerError;
import com.qna.domain.question.domain.entity.Question;
import com.qna.domain.user.domain.entity.User;
import com.qna.global.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Boolean isAccepted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Answer(String content, Question question, User user) {
        this.content = content;
        this.question = question;
        this.user = user;
    }

    public void accept() {
        if (this.isAccepted) {
            throw new BusinessException(AnswerError.ALREADY_ACCEPTED);
        }
        this.isAccepted = true;
        this.question.markAsAccepted();
    }

    public void update(String content) {
        this.content = content;
    }

    public boolean isOwner(Long userId) {
        return this.user.getId().equals(userId);
    }

    public boolean isQuestionOwner(Long userId) {
        return this.question.isOwner(userId);
    }
}
