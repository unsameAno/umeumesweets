package com.umeume.umeumesweets.service;

import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // -------------------
    // 파일 업로드
    // -------------------

    public String saveProductImage(MultipartFile imageFile) throws IOException {
        String uploadDir = new ClassPathResource("static/images/product/").getFile().getAbsolutePath();
        String fileName = imageFile.getOriginalFilename();
        File dest = new File(uploadDir, fileName);
        imageFile.transferTo(dest);
        return "/images/product/" + fileName;
    }

    // -------------------
    // 상품 CRUD
    // -------------------

    public void createProduct(Product product, MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = saveProductImage(imageFile);
                product.setImageUrl(imageUrl);
            }
            productRepository.save(product);
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 중 오류 발생", e);
        }
    }

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
                String imageUrl = saveProductImage(updated.getImageFile());
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

    // -------------------
    // 상품 조회
    // -------------------

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

    // -------------------
    // 필터 + 정렬 처리
    // -------------------

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

    // -------------------
    // 상품 검색
    // -------------------
    public List<Product> searchByKeyword(String keyword) {
    if (keyword != null && keyword.trim().length() >= 2) {
        return productRepository.findByNameContainingIgnoreCaseOrShop_ShopNameContainingIgnoreCase(keyword, keyword);
    } else {
        return productRepository.findAll();
    }
}
}
