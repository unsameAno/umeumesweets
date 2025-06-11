package com.umeume.umeumesweets.repository;

import com.umeume.umeumesweets.entity.FavoriteProduct;
import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {

    Optional<FavoriteProduct> findByUserAndProduct(User user, Product product);

    List<FavoriteProduct> findAllByUser(User user);

    boolean existsByUserAndProduct(User user, Product product);
}
