
package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.*;
import com.umeume.umeumesweets.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;

        // 장바구니에서 주문 페이지로 이동
    @GetMapping
    public String orderPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        List<CartItem> cartItems = cartService.getCartItems(user);
        model.addAttribute("cartItems", cartItems);
        return "orders/order-form";
    }

    // 상세페이지에서 '바로구매' 했을 때 주문 페이지로 이동
    @GetMapping("/direct")
    public String directOrderPage(HttpSession session, Model model) {
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("directPurchase");
        Integer totalPrice = (Integer) session.getAttribute("directTotalPrice");

        if (cartItems == null || totalPrice == null) {
            return "redirect:/"; // 잘못된 접근 방지
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        return "orders/order-form";
    }
}
