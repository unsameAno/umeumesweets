package com.umeume.umeumesweets.entity;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = true)
    private String category;

    @Transient
    private MultipartFile imageFile;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private int likeCount = 0;

    @Column(nullable = false)
    private double averageRating = 0.0;

    @Column(nullable = false)
    private int ratingCount = 0;

    @Column(nullable = false)
    private boolean isActive = true;
}
