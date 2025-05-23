package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.service.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class AdminProductController {

    private final ProductService productService;

    // ✅ 상품 등록 폼
    @GetMapping("/new")
    public String showProductCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product-form";
    }

    // ✅ 상품 리스트
    @GetMapping("/list")
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/product-list";
    }

    // ✅ 상품 수정 폼
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "admin/product-form";
    }

    // ✅ 수정 저장
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
        productService.updateProduct(id, product);
        return "redirect:/admin/product/list";
    }

    // ✅ 삭제
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/admin/product/list";
    }

@PostMapping("/new")
public String createProduct(@ModelAttribute Product product,
                            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
    // 저장할 경로 설정 (resources/static/images/product)
    String uploadDir = new ClassPathResource("static/images/product/").getFile().getAbsolutePath();
    String fileName = imageFile.getOriginalFilename();

    File dest = new File(uploadDir, fileName);
    imageFile.transferTo(dest);

    product.setImageUrl("/images/product/" + fileName); // ✅ 경로 수정
    productService.save(product);

    return "redirect:/admin/product/list";
}

}
