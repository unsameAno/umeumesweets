package com.umeume.umeumesweets.service;

import com.umeume.umeumesweets.dto.ProductDto;
import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.repository.FavoriteProductRepository;
import com.umeume.umeumesweets.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FavoriteProductRepository favoriteProductRepository;
    private final ImageUploadService imageUploadService; // ✅ Cloudinary 업로드용 서비스

    public List<ProductDto> getTop36ProductsWithLikeInfo(User user) {
        List<Product> topProducts = productRepository.findTop36ByOrderByLikeCountDesc();
        return topProducts.stream()
                .map(product -> toDto(product, user))
                .toList();
    }

    // ✅ 상품 등록
    public void createProduct(Product product, MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = imageUploadService.uploadImage(imageFile); // ✅ Cloudinary 업로드
                product.setImageUrl(imageUrl);
            }
            productRepository.save(product);
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 중 오류 발생", e);
        }
    }

    // ✅ 상품 수정
    public void updateProduct(Long id, Product updated) {
        Product existing = findById(id);
        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        existing.setPrice(updated.getPrice());
        existing.setDescription(updated.getDescription());
        existing.setStock(updated.getStock());
        existing.setStatus(updated.getStatus());
        existing.setShop(updated.getShop());

        if (updated.getImageFile() != null && !updated.getImageFile().isEmpty()) {
            try {
                String imageUrl = imageUploadService.uploadImage(updated.getImageFile()); // ✅ Cloudinary 업로드
                existing.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류 발생", e);
            }
        }

        productRepository.save(existing);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getProducts(Pageable pageable, String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return productRepository.findByNameContainingOrShop_ShopNameContaining(keyword, keyword, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    public List<Product> findSorted(String category, String sortOption) {
        String[] parts = sortOption.split("_");
        String field = parts[0];
        String directionStr = parts.length > 1 ? parts[1] : "desc";

        Sort.Direction direction = directionStr.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        String sortByField = switch (field) {
            case "price" -> "price";
            case "rating" -> "averageRating";
            case "wish" -> "wishCount";
            case "created" -> "createdAt";
            default -> "createdAt";
        };

        Sort sort = Sort.by(direction, sortByField);

        if (category == null || category.isBlank()) {
            return productRepository.findAll(sort);
        } else {
            return productRepository.findByCategory(category, sort);
        }
    }

    public List<ProductDto> findSorted(String category, String sortOption, User user) {
        List<Product> products = findSorted(category, sortOption);
        return products.stream()
                .map(product -> toDto(product, user))
                .collect(Collectors.toList());
    }

    public List<Product> searchByKeyword(String keyword) {
        if (keyword != null && keyword.trim().length() >= 2) {
            return productRepository.findByNameContainingIgnoreCaseOrShop_ShopNameContainingIgnoreCase(keyword, keyword);
        } else {
            return productRepository.findAll();
        }
    }

    public List<ProductDto> searchByKeyword(String keyword, User user) {
        List<Product> products = searchByKeyword(keyword);
        return products.stream()
                .map(product -> toDto(product, user))
                .collect(Collectors.toList());
    }

    public List<ProductDto> getAllDessertsWithLikeInfo(User user) {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> toDto(product, user))
                .collect(Collectors.toList());
    }

    public List<ProductDto> getAllProductsWithLikeInfo(User user) {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> toDto(product, user))
                .toList();
    }

    private ProductDto toDto(Product product, User user) {
        boolean liked = user != null && favoriteProductRepository.existsByUserAndProduct(user, product);

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setImageUrl(product.getImageUrl());
        dto.setPrice(product.getPrice());
        dto.setShopName(product.getShopName());
        dto.setAverageRating((float) product.getAverageRating());
        dto.setLiked(liked);

        return dto;
    }
}
