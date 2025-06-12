package com.umeume.umeumesweets.repository;

import com.umeume.umeumesweets.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}