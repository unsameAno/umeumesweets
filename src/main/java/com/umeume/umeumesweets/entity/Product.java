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
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column
    private String category;

    @Column
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private double averageRating;

    @Column(nullable = false)
    private int ratingCount;

    @Column(nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private DessertShop shop;

    // ★ MultipartFile은 Entity에 넣지 않는 것이 일반적 (Form/DTO에서만 처리)
    @Transient
    private transient MultipartFile imageFile;

    public String getShopName() {
        return shop != null ? shop.getShopName() : null;
    }

    public void setShopName(String name) {
        if (this.shop != null) {
            this.shop.setShopName(name);
        }
    }
}
