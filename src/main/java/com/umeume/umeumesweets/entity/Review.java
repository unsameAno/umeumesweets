package com.umeume.umeumesweets.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.entity.BaseTimeEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;  // 1~5점 사이

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Object builder() {
        throw new UnsupportedOperationException("Unimplemented method 'builder'");
    }
}
