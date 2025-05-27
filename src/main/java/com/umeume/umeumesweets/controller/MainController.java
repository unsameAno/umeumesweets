package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.entity.DessertShop;
import com.umeume.umeumesweets.repository.ProductRepository;
import com.umeume.umeumesweets.repository.DessertShopRepository; // 임포트 추가
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
    private final DessertShopRepository dessertShopRepository; // 주입된 객체

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        Object loginUser = session.getAttribute("loginUser");
        model.addAttribute("loginUser", loginUser);

        // 💡 DB에서 실제 상품 리스트 가져오기
        List<Product> desserts = productRepository.findAll();
        model.addAttribute("desserts", desserts);

        // 💡 DB에서 인기 카페 리스트 가져오기
        List<DessertShop> dessertShops = dessertShopRepository.findAll(); 
        model.addAttribute("dessertShops", dessertShops);

        return "index";
    }
}
