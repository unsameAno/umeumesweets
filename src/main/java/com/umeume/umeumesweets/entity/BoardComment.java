package com.umeume.umeumesweets.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    private BoardPost post;

    // 회원 작성자 (null이면 비회원)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // 비회원 작성자 닉네임
    private String guestName;

    // 비회원 비밀번호 (암호화되어 저장)
    private String guestPassword;

    // 댓글 본문
    @Lob
    private String content;

    // 생성 시각
    @CreationTimestamp
    private LocalDateTime createdAt;
}
