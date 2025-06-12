
package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.*;
import com.umeume.umeumesweets.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;

    // 장바구니에서 주문 페이지로 이동
    @GetMapping
public String orderPage(@RequestParam(required = false) String cartItemIds,
                        Model model, HttpSession session) {
    User user = (User) session.getAttribute("loginUser");

    List<CartItem> allItems = cartService.getCartItems(user);
    List<CartItem> selectedItems;

    if (cartItemIds != null && !cartItemIds.isBlank()) {
        List<Long> selectedIds = Arrays.stream(cartItemIds.split(","))
            .map(Long::parseLong)
            .toList();

        selectedItems = allItems.stream()
            .filter(item -> selectedIds.contains(item.getId()))
            .toList();
    } else {
        selectedItems = allItems;
    }

    int totalPrice = selectedItems.stream()
        .mapToInt(i -> i.getProduct().getPrice() * i.getQuantity())
        .sum();

    model.addAttribute("cartItems", selectedItems);
    model.addAttribute("totalPrice", totalPrice);
    return "orders/order-form";
}


@GetMapping("/direct")
public String orderDirectPage(HttpSession session, Model model) {
    List<CartItem> items = (List<CartItem>) session.getAttribute("directPurchase");
    int totalPrice = (int) session.getAttribute("directTotalPrice");

    model.addAttribute("cartItems", items);
    model.addAttribute("totalPrice", totalPrice);

    return "orders/order-form";
}

    
}
