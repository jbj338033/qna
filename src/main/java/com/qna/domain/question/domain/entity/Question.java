package com.qna.domain.question.domain.entity;

import com.qna.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long viewCount = 0L;

    @Column(nullable = false)
    private Boolean hasAcceptedAnswer = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Question(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void markAsAccepted() {
        this.hasAcceptedAnswer = true;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public boolean isOwner(Long userId) {
        return this.user.getId().equals(userId);
    }
}
