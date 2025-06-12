package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.dto.ProductDto;
import com.umeume.umeumesweets.entity.CartItem;
import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.entity.Review;
import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.repository.DessertShopRepository;
import com.umeume.umeumesweets.repository.ProductRepository;
import com.umeume.umeumesweets.service.ProductService;
import com.umeume.umeumesweets.service.ReviewService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final DessertShopRepository dessertShopRepository;
    private final ProductService productService;
    private final ReviewService reviewService;

    // ✅ 상품 목록 페이지 (카테고리 + 정렬 + 찜 여부 포함)
    @GetMapping
    public String getProducts(@RequestParam(required = false) String category,
                              @RequestParam(defaultValue = "created_desc") String sort,
                              Model model,
                              HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        List<ProductDto> products = productService.findSorted(category, sort, user);
        model.addAttribute("desserts", products);
        return "products/product-list";
    }

    // ✅ 상품 상세 페이지
    @GetMapping("/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        List<Review> reviews = reviewService.getReviewsByProductId(id);
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        return "products/product-detail";
    }

    // ✅ 상품 검색 (찜 여부 포함)
    @GetMapping("/search")
    public String searchProducts(@RequestParam(required = false) String keyword,
                                  Model model,
                                  HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        List<ProductDto> products = productService.searchByKeyword(keyword, user);
        model.addAttribute("desserts", products);
        model.addAttribute("keyword", keyword);
        return "products/product-list";
    }

    // ✅ 바로구매 기능
    @PostMapping("/direct")
    @ResponseBody
    public ResponseEntity<String> directOrder(@RequestParam Long productId,
                                              @RequestParam int quantity,
                                              HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        Product product = productService.findById(productId);

        CartItem fakeItem = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .build();

        session.setAttribute("directPurchase", List.of(fakeItem));
        session.setAttribute("directTotalPrice", product.getPrice() * quantity);

        return ResponseEntity.ok("/order/direct");
    }
}
