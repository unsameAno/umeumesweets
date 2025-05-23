package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.Product;
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

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        Object loginUser = session.getAttribute("loginUser");
        model.addAttribute("loginUser", loginUser);

        // 💡 DB에서 실제 상품 리스트 가져오기
        List<Product> desserts = productRepository.findAll();
        model.addAttribute("desserts", desserts);

        return "index";
    }
}
