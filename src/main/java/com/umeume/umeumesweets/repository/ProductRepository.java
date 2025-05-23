package com.umeume.umeumesweets.repository;

import com.umeume.umeumesweets.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 원하는 가게 이름으로 상품 조회!
    List<Product> findByShopName(String shopName);

    // 카테고리별 디저트 조회도 가능!
    List<Product> findByCategory(String category);

    // 현재 판매중인 상품만!
    List<Product> findByIsActiveTrue();
}
