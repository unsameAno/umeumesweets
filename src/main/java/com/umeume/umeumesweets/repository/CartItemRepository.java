package com.umeume.umeumesweets.repository;

import com.umeume.umeumesweets.entity.CartItem;
import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndProduct(User user, Product product);

    void deleteByIdAndUser(Long id, User user); 

}
