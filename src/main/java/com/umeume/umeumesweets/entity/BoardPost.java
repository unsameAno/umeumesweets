package com.umeume.umeumesweets.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원 사용자 (nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String title;

    private String category; // "디저트 추천", "카페 추천"

    @Lob
    private String content;

    // 비회원용 닉네임
    private String guestName;

    // 비회원용 비밀번호 (BCrypt 해시로 저장)
    private String guestPassword;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardComment> comments;
}
