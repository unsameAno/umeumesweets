package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.CartItem;
import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam int quantity,
                            HttpSession session) {

        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            // 로그인하지 않은 경우 → 로그인 페이지로 이동
            return "redirect:/login";
        }

        try {
            cartService.addToCart(user, productId, quantity);
        } catch (Exception e) {
            e.printStackTrace(); // 실제 운영 땐 로깅 처리
            return "redirect:/error";
        }

        return "redirect:/cart";
    }

    @GetMapping
    public String showCart(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");

        if (user == null) {
            return "redirect:/login";
        }

        List<CartItem> cartItems;
        try {
            cartItems = cartService.getCartItems(user);
        } catch (Exception e) {
            cartItems = Collections.emptyList(); // 예외 시 빈 목록 처리
        }

        model.addAttribute("cartItems", cartItems);
        return "cart-list";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user != null) {
            try {
                cartService.clearCart(user);
            } catch (Exception e) {
                e.printStackTrace(); // 운영 시 로깅 또는 에러 페이지
            }
        }
        return "redirect:/cart";
    }
}
