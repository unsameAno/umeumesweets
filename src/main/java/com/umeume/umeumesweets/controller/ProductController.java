package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.repository.DessertShopRepository;
import com.umeume.umeumesweets.repository.ProductRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final DessertShopRepository dessertShopRepository;

@PostConstruct
public void init() {
    System.out.println("✅ ProductController Loaded!");
}

    // ✅ 사용자용 상품 전체 리스트
    @GetMapping("/product-list")
    public String showProductList(Model model) {
        List<Product> productList = productRepository.findAll();
        model.addAttribute("desserts", productList); // 💡 템플릿에서는 ${desserts}로 받음
        return "products/product-list"; // templates/products/product-list.html
    }

    // ✅ 사용자용 상품 상세 페이지
    @GetMapping("/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        model.addAttribute("product", product);
        return "products/detail"; // templates/products/detail.html
    }
}
