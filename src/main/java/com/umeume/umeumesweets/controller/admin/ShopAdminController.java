package com.umeume.umeumesweets.controller.admin;

import com.umeume.umeumesweets.entity.DessertShop;
import com.umeume.umeumesweets.service.DessertShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/shop")
public class ShopAdminController {

    private final DessertShopService dessertShopService;

    // 가게 등록 폼
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("shop", new DessertShop());
        return "admin/shop-form";
    } 

    // 가게 등록 처리
    @PostMapping("/new")
    public String createShop(@ModelAttribute DessertShop shop,
                             @RequestParam("image") MultipartFile imageFile) {
        dessertShopService.createShop(shop, imageFile);
        return "redirect:/admin/shop/list?success";
    }

    // 가게 리스트 with 페이징 + 검색
    @GetMapping("/list")
    public String showShopList(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size,
                               @RequestParam(required = false) String keyword,
                               Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DessertShop> shopPage = dessertShopService.getShops(pageable, keyword);
        model.addAttribute("shopPage", shopPage);
        model.addAttribute("keyword", keyword);
        return "admin/shop-list";
    }

    // 가게 수정 폼
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("shop", dessertShopService.findById(id));
        return "admin/shop-form";
    }

    // 가게 수정 처리
    @PostMapping("/edit/{id}")
    public String updateShop(@PathVariable Long id,
                            @ModelAttribute DessertShop updatedShop,
                            @RequestParam("image") MultipartFile imageFile) {
        dessertShopService.updateShop(id, updatedShop, imageFile);
        return "redirect:/admin/shop/list";
    }

    // 가게 삭제
    @GetMapping("/delete/{id}")
    public String deleteShop(@PathVariable Long id) {
        dessertShopService.deleteById(id);
        return "redirect:/admin/shop/list";
    }
}
