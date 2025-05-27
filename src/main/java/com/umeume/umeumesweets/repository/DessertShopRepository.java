package com.umeume.umeumesweets.repository;

import com.umeume.umeumesweets.entity.DessertShop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DessertShopRepository extends JpaRepository<DessertShop, Long> {

    // 🔍 가게 이름으로 검색하는 쿼리 메서드
    Page<DessertShop> findByShopNameContaining(String keyword, Pageable pageable);

}
