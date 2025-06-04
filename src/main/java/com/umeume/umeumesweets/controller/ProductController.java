package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.repository.DessertShopRepository;
import com.umeume.umeumesweets.repository.ProductRepository;
import com.umeume.umeumesweets.service.ProductService;
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
    private final ProductService productService;

    // ✅ 상품 목록 페이지 (카테고리 + 정렬 포함)
    @GetMapping
    public String getProducts(
        @RequestParam(required = false) String category,
        @RequestParam(defaultValue = "created_desc") String sort,
        Model model) {

        List<Product> products = productService.findSorted(category, sort);
        model.addAttribute("desserts", products);
        return "products/product-list";
    }

    // ✅ 상품 상세 페이지
    @GetMapping("/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "products/detail";
    }
}
