package com.umeume.umeumesweets.controller.admin;

import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.repository.DessertShopRepository;
import com.umeume.umeumesweets.service.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class DessertProductController {

    private final ProductService productService;
    private final DessertShopRepository dessertShopRepository;

    @GetMapping("/new")
    public String showProductCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("shops", dessertShopRepository.findAll());
        return "admin/product-form";
    }

    @GetMapping("/list")
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/product-list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "admin/product-form";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
        productService.updateProduct(id, product);
        return "redirect:/admin/product/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/admin/product/list";
    }

    @PostMapping("/new")
    public String createProduct(@ModelAttribute Product product,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        String uploadDir = new ClassPathResource("static/images/product/").getFile().getAbsolutePath();
        String fileName = imageFile.getOriginalFilename();

        File dest = new File(uploadDir, fileName);
        imageFile.transferTo(dest);

        product.setImageUrl("/images/product/" + fileName);
        productService.save(product);

        return "redirect:/admin/product/list";
    }
}
