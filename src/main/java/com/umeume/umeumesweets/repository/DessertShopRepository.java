package com.umeume.umeumesweets.repository;

import com.umeume.umeumesweets.entity.DessertShop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DessertShopRepository extends JpaRepository<DessertShop, Long> {

    // 🔥 추가된 별점순 10개 조회
    List<DessertShop> findTop10ByOrderByAverageRatingDesc();
}