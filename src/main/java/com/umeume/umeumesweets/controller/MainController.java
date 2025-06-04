package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.DessertShop;
import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.repository.DessertShopRepository;
import com.umeume.umeumesweets.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductRepository productRepository;
    private final DessertShopRepository dessertShopRepository;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        Object loginUser = session.getAttribute("loginUser");
        model.addAttribute("loginUser", loginUser);

        // ✅ 인기순으로 상품 20개만 (찜 많은 순)
        List<Product> desserts = productRepository.findTop20ByOrderByLikeCountDesc();
        model.addAttribute("desserts", desserts);

        // ✅ 평균 별점 높은 카페 10개만
        List<DessertShop> dessertShops = dessertShopRepository.findTop10ByOrderByAverageRatingDesc();
        model.addAttribute("dessertShops", dessertShops);

        return "index";
    }
}
