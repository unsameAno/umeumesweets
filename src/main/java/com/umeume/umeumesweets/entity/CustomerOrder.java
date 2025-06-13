// CustomerOrder.java
package com.umeume.umeumesweets.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;


@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerOrder extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문자 (로그인 유저)
    @ManyToOne
    private User user;

    // 수령자 정보
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipient_id")
    private OrderRecipient recipient;

    // 주문일시
    private LocalDateTime orderDate;

    // 결제수단 (enum 고려)
    private String paymentMethod;

    // 총 결제 금액
    private int totalAmount;
    
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 주문상품 목록
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;



    public int getTotalPrice() {
    return items.stream()
            .mapToInt(item -> item.getPrice() * item.getQuantity())
            .sum();
    }

    @Column(name = "order_status")
    private String status; // 예: "NEW", "PAID", "CANCELED", "SHIPPED" 등

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCompleted() {
        return "PAID".equals(this.status) || "SHIPPED".equals(this.status);
    }

}

