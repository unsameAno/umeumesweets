package com.umeume.umeumesweets.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 주문에 속했는지
    @ManyToOne
    @JoinColumn(name = "order_id")
    private CustomerOrder order;

    // 어떤 상품인지
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // 주문 당시 상품 이름 (스냅샷)
    private String productName;

    // 주문 당시 카페 이름 (스냅샷)
    private String shopName;

    // 수량
    private int quantity;

    // 단가
    private int price;
}
