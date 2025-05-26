package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.repository.DessertShopRepository;
import com.umeume.umeumesweets.repository.ProductRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository productRepository;
    private final DessertShopRepository dessertShopRepository;
    

    @PostConstruct
    public void checkShops() {
        System.out.println("🍰 전체 가게 수: " + dessertShopRepository.count());
        dessertShopRepository.findAll()
            .forEach(shop -> System.out.println("🍰 가게: " + shop.getId() + " / " + shop.getShopName()));
    }

    // 🧁 상품 등록 폼
    @GetMapping("/new")
    public String showProductForm(Model model) {
    System.out.println("💥 [GET] /product/new 진입!");

    model.addAttribute("product", new Product());

    dessertShopRepository.findAll().forEach(s -> 
        System.out.println("🍰 가게 이름: " + s.getShopName())
    );

    model.addAttribute("shops", dessertShopRepository.findAll());
    return "admin/product-form";
    }

    // 🍩 상품 저장 처리
    @PostMapping
    public String saveProduct(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/product/list";
    }

    // 🍡 상품 목록 페이지
    @GetMapping("/list")
    public String showProductList(Model model) {
        List<Product> productList = productRepository.findAll();
        model.addAttribute("products", productList);
        return "product/list"; // templates/product/list.html
    }

    @GetMapping("/{id}")
public String showProductDetail(@PathVariable Long id, Model model) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    model.addAttribute("product", product);
    return "product/detail"; // templates/product/detail.html
}

}
