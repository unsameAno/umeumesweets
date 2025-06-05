package com.umeume.umeumesweets.repository;

import com.umeume.umeumesweets.entity.Review;
import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product product);

    List<Review> findByUser(User user);

    Long countByProduct(Product product);

    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);
    
    List<Review> findAllByProduct_Id(Long productId);
}
