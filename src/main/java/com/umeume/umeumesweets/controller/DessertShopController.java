package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.DessertShop;
import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.repository.DessertShopRepository;
import com.umeume.umeumesweets.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shops")
public class DessertShopController {

    private final DessertShopRepository dessertShopRepository;
    private final ProductRepository productRepository;

    /**
     * 디저트 카페 전체 리스트 페이지
     * URL: /shops
     */
    @GetMapping
    public String showShopList(Model model) {
        List<DessertShop> shopList = dessertShopRepository.findAll();
        model.addAttribute("dessertShops", shopList);
        return "shops/shop-list";
    }

    /**
     * 디저트 카페 상세 페이지
     * URL: /shops/{id}
     */
    @GetMapping("/{id}")
    public String showShopDetail(@PathVariable("id") Long shopId, Model model) {
        DessertShop shop = dessertShopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 카페를 찾을 수 없습니다: " + shopId));

        List<Product> productList = productRepository.findByShopId(shopId);

        model.addAttribute("shop", shop);
        model.addAttribute("products", productList);
        return "shops/shop-detail";
    }
}
