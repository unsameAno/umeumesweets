package com.umeume.umeumesweets.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DessertShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String zipcode;

    @Column(nullable = false)
    private String address;

    @Column
    private String detailAddress;

    @Column
    private String description;

    @Column
    private String imageUrl;

    @Builder.Default
    @Column(nullable = false)
    private int likeCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private double averageRating = 0.0;

    @Builder.Default
    @Column(nullable = false)
    private int ratingCount = 0;

    // ✨ Product에서 shop.getName() 가능하게!
    public String getName() {
        return this.shopName;
    }

    public void setName(String name) {
        this.shopName = name;
    }

    public String getShopName() {
    return this.shopName;
    }
}
