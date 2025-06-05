package com.umeume.umeumesweets.service;

import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.entity.Review;
import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.repository.ProductRepository;
import com.umeume.umeumesweets.repository.ReviewRepository;
import com.umeume.umeumesweets.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public Review createReview(Long productId, Long userId, int rating, String content) {
        Product product = getProductOrThrow(productId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(rating);
        review.setContent(content);

        reviewRepository.save(review);

        // 리뷰 통계 업데이트
        List<Review> reviews = reviewRepository.findAllByProduct_Id(productId);
        int totalRating = reviews.stream().mapToInt(Review::getRating).sum();
        int reviewCount = reviews.size();
        double averageRating = (double) totalRating / reviewCount;

        product.setAverageRating(averageRating);
        product.setRatingCount(reviewCount);
        System.out.println(">>> 평균 별점 계산됨: " + averageRating);
        System.out.println(">>> 저장 직전 product ID: " + product.getId());
        productRepository.save(product);  // 꼭 저장해야 DB 반영됨

        return review;
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    @Transactional(readOnly = true)
    public Product getProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }
}
