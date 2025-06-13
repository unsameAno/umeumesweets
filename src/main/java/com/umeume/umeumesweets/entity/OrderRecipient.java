package com.umeume.umeumesweets.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRecipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 수령인 이름
    @Column(nullable = false)
    private String receiverName;

    // 연락처
    @Column(nullable = false)
    private String phone;

    // 우편번호
    @Column(nullable = false)
    private String zipcode;

    // 주소
    @Column(nullable = false)
    private String address;

    // 상세주소
    @Column
    private String addressDetail;

    // 주문과의 연관관계 (1:1)
    @OneToOne(mappedBy = "recipient", cascade = CascadeType.ALL)
    private CustomerOrder order;
}
