package com.umeume.umeumesweets.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

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

    @Column
    private String description;

    @Builder.Default
    @Column(nullable = false)
    private int stock = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Builder.Default
    @Column(nullable = false)
    private int likeCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private double averageRating = 0.0;

    @Builder.Default
    @Column(nullable = false)
    private int ratingCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private DessertShop shop;

    @Transient
    private MultipartFile imageFile;

    public String getShopName() {
        return shop != null ? shop.getShopName() : null;
    }

    public void setShopName(String name) {
        if (this.shop != null) {
            this.shop.setShopName(name);
        }
    }
}
