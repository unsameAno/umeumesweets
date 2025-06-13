// OrderService.java
package com.umeume.umeumesweets.service;

import com.umeume.umeumesweets.dto.OrderRequestDto;
import com.umeume.umeumesweets.entity.*;
import com.umeume.umeumesweets.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerOrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional
public CustomerOrder placeOrder(OrderRequestDto dto, User user) {
    if (dto.getItems() == null || dto.getItems().isEmpty()) {
    throw new IllegalArgumentException("주문 상품 정보가 없습니다.");
}
    // 1. 수령자 정보 저장
    OrderRecipient recipient = OrderRecipient.builder()
            .receiverName(dto.getReceiverName())
            .phone(dto.getPhone())
            .zipcode(dto.getZipcode())
            .address(dto.getAddress())
            .addressDetail(dto.getAddressDetail())
            .build();

    // 2. 상품 정보 가져오기
    List<OrderItem> orderItems = dto.getItems().stream().map(itemDto -> {
        Product product = productRepository.findById(itemDto.getProductId())
                .orElseThrow(() -> new RuntimeException("상품 없음"));
        return OrderItem.builder()
                .product(product)
                .productName(product.getName())
                .shopName(product.getShop().getName()) // getShop() 메서드 있는지 확인
                .price(product.getPrice())
                .quantity(itemDto.getQuantity())
                .build();
    }).toList();

    // 3. 총액 계산
    int totalAmount = orderItems.stream()
            .mapToInt(item -> item.getPrice() * item.getQuantity())
            .sum();

    // 4. 주문 생성
    CustomerOrder order = CustomerOrder.builder()
            .user(user)
            .recipient(recipient)
            .orderDate(LocalDateTime.now())
            .paymentMethod(dto.getPaymentMethod())
            .totalAmount(totalAmount)
            .status("PAID")
            .items(orderItems)
            .build();

    // 5. 역참조 설정
    for (OrderItem item : orderItems) {
        item.setOrder(order);
    }

    return orderRepository.save(order);
}

public List<CustomerOrder> getOrdersByUser(User user) {
    return orderRepository.findByUser(user);
}

public CustomerOrder getLastOrderByUser(User user) {
    return orderRepository.findTopByUserOrderByCreatedAtDesc(user)
        .orElseThrow(() -> new IllegalStateException("주문 내역이 없습니다."));
}

@Transactional
    public void cancelById(Long id) {
        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 주문이 존재하지 않습니다: " + id));

        // 주문 상태가 이미 완료되었는지 확인 (선택)
        if (order.isCompleted()) {
            throw new IllegalStateException("이미 완료된 주문은 취소할 수 없습니다.");
        }

        // 주문 상태를 "취소됨"으로 변경
        order.setStatus("CANCELED"); // 또는 Enum 사용 시: order.setStatus(OrderStatus.CANCELED);

        // 필요한 경우 재고 복원, 포인트 환불 등 추가 처리 가능
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        CustomerOrder order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NoSuchElementException("주문 없음"));

        order.setStatus("CANCELED");
    }

}
