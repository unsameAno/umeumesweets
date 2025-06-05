package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.entity.Review;
import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.repository.ProductRepository;
import com.umeume.umeumesweets.repository.ReviewRepository;
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
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    // 특정 상품의 리뷰 목록
    @GetMapping("/product/{productId}")
    public String getReviewsByProduct(@PathVariable Long productId,
                                      Model model,
                                      HttpSession session) {
        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        Product product = reviewService.getProductOrThrow(productId);
        User loginUser = (User) session.getAttribute("loginUser");

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("loginUser", loginUser);

        return "products/detail.html";
    }

    // 리뷰 작성 폼
    @GetMapping("/new/{productId}")
    public String showReviewForm(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        model.addAttribute("review", new Review());
        return "review/form";
    }

    // 리뷰 등록 처리
    @PostMapping("/new")
    public String submitReview(@RequestParam Long productId,
                               @RequestParam int rating,
                               @RequestParam String content,
                               HttpSession session) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login?message=login_required";
        }

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(rating);
        review.setContent(content);

        reviewService.createReview(productId, user.getId(), rating, content);

        return "redirect:/reviews/product/" + productId + "?tab=review";
    }

    // 리뷰 삭제 폼
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("리뷰 없음"));

        if (!review.getUser().getId().equals(loginUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한 없음");
        }

        reviewRepository.delete(review);
        return ResponseEntity.ok().build();
    }
}
