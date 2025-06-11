package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.dto.ProductDto;
import com.umeume.umeumesweets.entity.DessertShop;
import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.repository.DessertShopRepository;
import com.umeume.umeumesweets.repository.ProductRepository;
import com.umeume.umeumesweets.service.ProductService;

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
    private final ProductService productService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        Object loginUser = session.getAttribute("loginUser");
        model.addAttribute("loginUser", loginUser);

        User user = null;
        if (loginUser instanceof User u) {
            user = u;
        }

        // ✅ ProductDto로 찜 여부까지 포함해서 가져오기
        List<ProductDto> desserts = productService.getTop20ProductsWithLikeInfo(user);
        model.addAttribute("desserts", desserts);

        List<DessertShop> dessertShops = dessertShopRepository.findTop10ByOrderByAverageRatingDesc();
        model.addAttribute("dessertShops", dessertShops);

        return "index";
    }
}
