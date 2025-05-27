package com.umeume.umeumesweets.repository;

import com.umeume.umeumesweets.entity.DessertShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DessertShopRepository extends JpaRepository<DessertShop, Long> {
    // 추가적인 쿼리 메서드를 정의할 수 있습니다.
}
