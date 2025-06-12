// CustomerOrderRepository.java
package com.umeume.umeumesweets.repository;

import com.umeume.umeumesweets.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
}
