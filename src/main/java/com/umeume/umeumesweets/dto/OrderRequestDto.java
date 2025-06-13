package com.umeume.umeumesweets.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    private String receiverName;
    private String phone;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String paymentMethod;
    private List<OrderItemDto> items;
}
