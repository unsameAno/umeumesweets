// CustomerOrderRepository.java
package com.umeume.umeumesweets.repository;

import com.umeume.umeumesweets.entity.CustomerOrder;
import com.umeume.umeumesweets.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByUser(User user);
    Optional<CustomerOrder> findTopByUserOrderByCreatedAtDesc(User user);
}


