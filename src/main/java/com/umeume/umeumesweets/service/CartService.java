package com.umeume.umeumesweets.service;

import com.umeume.umeumesweets.entity.CartItem;
import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.repository.CartItemRepository;
import com.umeume.umeumesweets.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addToCart(User user, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product)
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(quantity)
                    .build();
        }

        cartItemRepository.save(cartItem);
    }

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

        @Transactional
    public void deleteItemById(User user, Long cartItemId) {
        cartItemRepository.findById(cartItemId).ifPresent(item -> {
            if (item.getUser().getId().equals(user.getId())) {
                cartItemRepository.delete(item);
            }
        });
    }

    @Transactional
    public void deleteItemsByIds(User user, List<Long> ids) {
        List<CartItem> items = cartItemRepository.findAllById(ids);
        for (CartItem item : items) {
            if (item.getUser().getId().equals(user.getId())) {
                cartItemRepository.delete(item);
            }
        }
    }



}
