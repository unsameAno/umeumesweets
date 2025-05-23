package com.umeume.umeumesweets.service;

import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    public void updateProduct(Long id, Product updated) {
        Product existing = findById(id);
        existing.setName(updated.getName());
        existing.setPrice(updated.getPrice());
        existing.setShopName(updated.getShopName());
        existing.setImageUrl(updated.getImageUrl());
        productRepository.save(existing);
    }

    // 상품등록 저장 
    public void save(Product product) {
    productRepository.save(product);
    }

}
