package com.umeume.umeumesweets.repository;

import com.umeume.umeumesweets.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByShop_ShopName(String shopName);

    List<Product> findByCategory(String category);

    List<Product> findByIsActiveTrue();

    Page<Product> findByNameContainingOrShop_ShopNameContaining(String name, String shopName, Pageable pageable);

    // 🔥 추가된 인기순 20개 조회
    List<Product> findTop20ByOrderByLikeCountDesc();

    List<Product> findByCategory(String category, Sort sortBy);

    
}