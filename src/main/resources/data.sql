INSERT INTO users (email, password, nickname, created_at) VALUES
('admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqQb9nZaT1.RCvL0CZLRCXt8OKO5a', '관리자', NOW()),
('user1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqQb9nZaT1.RCvL0CZLRCXt8OKO5a', '김개발', NOW()),
('user2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqQb9nZaT1.RCvL0CZLRCXt8OKO5a', '이코딩', NOW()),
('user3@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqQb9nZaT1.RCvL0CZLRCXt8OKO5a', '박자바', NOW());

INSERT INTO tags (name) VALUES
('java'),
('spring'),
('jpa'),
('security'),
('rest-api'),
('database'),
('test');

INSERT INTO questions (title, content, view_count, has_accepted_answer, user_id, created_at, updated_at) VALUES
('Spring Boot에서 JPA N+1 문제 해결 방법', 'JPA를 사용하다가 N+1 문제가 발생했습니다. 어떻게 해결할 수 있나요? Fetch Join이나 EntityGraph를 사용해야 하나요?', 45, false, 2, NOW(), NOW()),
('JWT 토큰 만료 처리 방법', 'JWT 토큰이 만료되었을 때 클라이언트에서 어떻게 처리해야 하나요? Refresh Token을 사용해야 하나요?', 32, true, 3, NOW(), NOW()),
('Spring Security 권한 설정 질문', '@PreAuthorize와 @Secured의 차이점이 무엇인가요? 어떤 것을 사용하는 것이 좋을까요?', 28, false, 4, NOW(), NOW()),
('REST API 설계 시 URL 네이밍 규칙', 'REST API를 설계할 때 URL은 어떻게 작성하는 것이 좋은가요? 복수형을 사용해야 하나요?', 56, true, 2, NOW(), NOW()),
('JPA에서 양방향 연관관계 설정', '양방향 연관관계를 설정할 때 주의할 점이 있나요? 순환 참조 문제는 어떻게 해결하나요?', 23, false, 3, NOW(), NOW());

INSERT INTO question_tags (question_id, tag_id) VALUES
(1, 2), (1, 3),
(2, 2), (2, 4),
(3, 2), (3, 4),
(4, 5),
(5, 3), (5, 6);

INSERT INTO answers (content, is_accepted, question_id, user_id, created_at) VALUES
('Fetch Join을 사용하면 N+1 문제를 해결할 수 있습니다. @Query 어노테이션에서 JOIN FETCH를 사용하세요.', false, 1, 3, NOW()),
('@EntityGraph를 사용하는 방법도 있습니다. attributePaths에 연관 엔티티를 지정하면 됩니다.', false, 1, 4, NOW()),
('Refresh Token을 사용하는 것이 일반적입니다. Access Token은 짧게, Refresh Token은 길게 설정하세요.', true, 2, 2, NOW()),
('@PreAuthorize는 SpEL을 지원해서 더 유연합니다. 복잡한 권한 체크가 필요하면 @PreAuthorize를 추천합니다.', false, 3, 2, NOW()),
('REST API URL은 복수형 명사를 사용하는 것이 일반적입니다. 예: /users, /questions', true, 4, 4, NOW()),
('동사는 HTTP 메서드로 표현하고, URL에는 리소스만 표현하세요.', false, 4, 3, NOW());
