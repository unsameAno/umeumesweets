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
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(rating);
        review.setContent(content);
        return reviewRepository.save(review);
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
