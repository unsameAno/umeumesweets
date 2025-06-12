// OrderService.java
package com.umeume.umeumesweets.service;

import com.umeume.umeumesweets.entity.*;
import com.umeume.umeumesweets.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerOrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public CustomerOrder placeOrder(User user, List<CartItem> cartItems, String paymentMethod) {
        int totalAmount = cartItems.stream()
                .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        CustomerOrder order = CustomerOrder.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .paymentMethod(paymentMethod)
                .totalAmount(totalAmount)
                .orderStatus("결제완료")
                .build();

        order = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getProduct().getPrice())
                    .build();
            orderItemRepository.save(orderItem);
        }

        cartItemRepository.deleteAll(cartItems); // 주문 완료 후 장바구니 비움

        return order;
    }
}
