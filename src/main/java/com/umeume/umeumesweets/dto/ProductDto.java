package com.umeume.umeumesweets.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String name;
    private String imageUrl;
    private int price;
    private String shopName;
    private float averageRating;
    private boolean liked;
}