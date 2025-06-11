package com.umeume.umeumesweets.service;

import com.umeume.umeumesweets.dto.ProductDto;
import com.umeume.umeumesweets.entity.FavoriteProduct;
import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.repository.FavoriteProductRepository;
import com.umeume.umeumesweets.repository.ProductRepository;
import com.umeume.umeumesweets.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteProductRepository favoriteProductRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean toggleFavorite(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        // 이미 찜했는지 확인
        return favoriteProductRepository.findByUserAndProduct(user, product)
                .map(fp -> {
                    favoriteProductRepository.delete(fp);
                    return false; // 삭제 후 false
                }).orElseGet(() -> {
                    FavoriteProduct newFav = new FavoriteProduct();
                    newFav.setUser(user);
                    newFav.setProduct(product);
                    favoriteProductRepository.save(newFav);
                    return true; // 추가 후 true
                });
    }

    public List<FavoriteProduct> getFavorites(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        return favoriteProductRepository.findAllByUser(user);
    }

    public boolean isFavorite(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        return user != null && product != null &&
                favoriteProductRepository.existsByUserAndProduct(user, product);
    }

    public List<ProductDto> getFavoriteProducts(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
    List<FavoriteProduct> favoriteProducts = favoriteProductRepository.findAllByUser(user);

    return favoriteProducts.stream().map(fp -> {
        Product product = fp.getProduct();
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setImageUrl(product.getImageUrl());
        dto.setPrice(product.getPrice());
        dto.setShopName(product.getShopName());
        dto.setAverageRating((float) product.getAverageRating());
        dto.setLiked(true);
        return dto;
    }).toList();
}

}
