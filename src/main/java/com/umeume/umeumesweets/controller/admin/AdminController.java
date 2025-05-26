package com.umeume.umeumesweets.controller.admin;

import com.umeume.umeumesweets.entity.DessertShop;
import com.umeume.umeumesweets.repository.DessertShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final DessertShopRepository dessertShopRepository;

    // 🔸 관리자 홈 (관리 메뉴)
@GetMapping
public String adminHome(Model model) {
    model.addAttribute("shop", new DessertShop()); // ⬅ 이게 있어야 form이 안 터져!
    return "admin/index";
}
}
