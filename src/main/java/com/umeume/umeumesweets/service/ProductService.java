package com.umeume.umeumesweets.service;

import com.umeume.umeumesweets.entity.Product;
import com.umeume.umeumesweets.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 이미지 저장
    public String saveProductImage(MultipartFile imageFile) throws IOException {
        String uploadDir = new ClassPathResource("static/images/product/").getFile().getAbsolutePath();
        String fileName = imageFile.getOriginalFilename();
        File dest = new File(uploadDir, fileName);
        imageFile.transferTo(dest);  // 파일 업로드
        return "/images/product/" + fileName;  // 저장된 파일 경로 반환
    }

    // 상품 등록 처리
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

    // 모든 상품 조회
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 페이징 및 검색된 상품 조회
    public Page<Product> getProducts(Pageable pageable, String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return productRepository.findByNameContainingOrShop_ShopNameContaining(keyword, keyword, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    // 상품 삭제
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    // 상품 조회 by ID
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    // 상품 수정 처리
    public void updateProduct(Long id, Product updated) {
        Product existing = findById(id);
        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        existing.setPrice(updated.getPrice());
        existing.setDescription(updated.getDescription());
        existing.setStock(updated.getStock());
        existing.setStatus(updated.getStatus());
        existing.setShop(updated.getShop());
        
        // 이미지 업데이트 처리
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

    // 상품 저장
    public void save(Product product) {
        productRepository.save(product);
    }
}
