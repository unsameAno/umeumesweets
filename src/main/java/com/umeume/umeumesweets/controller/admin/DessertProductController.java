package com.umeume.umeumesweets.controller.admin;

import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.service.ProductService;
import com.umeume.umeumesweets.repository.DessertShopRepository;
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
@RequestMapping("/admin/product")
public class DessertProductController {

    private final ProductService productService;
    private final DessertShopRepository dessertShopRepository;

    // 상품 등록 폼
    @GetMapping("/new")
    public String showProductCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("shops", dessertShopRepository.findAll());
        return "admin/product-form";
    }

    // 상품 생성 처리
    @PostMapping("/new")
    public String createProduct(@ModelAttribute Product product,
                                @RequestParam("imageFile") MultipartFile imageFile) {
        productService.createProduct(product, imageFile);
        return "redirect:/admin/product/list";
    }

    // 상품 리스트 with 페이징 + 검색
    @GetMapping("/list")
    public String showProductList(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size,
                                  @RequestParam(required = false) String keyword,
                                  Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.getProducts(pageable, keyword);
        model.addAttribute("productPage", productPage);
        model.addAttribute("keyword", keyword);
        return "admin/product-list";
    }

    // 상품 수정 폼
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("shops", dessertShopRepository.findAll());
        return "admin/product-form";
    }

    // 상품 수정 처리
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product updatedProduct) {
        productService.updateProduct(id, updatedProduct);
        return "redirect:/admin/product/list";
    }

    // 상품 삭제
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/admin/product/list";
    }

    // 🔥 제거 or 다른 URL로 변경 요망
    // 상품 상세 페이지 (기능이 필요 없다면 삭제해도 무방)
    // @GetMapping("/{id}")
    // public String showProductDetail(@PathVariable Long id, Model model) {
    //     Product product = productService.findById(id);
    //     model.addAttribute("product", product);
    //     return "admin/product-form";
    // }
}
