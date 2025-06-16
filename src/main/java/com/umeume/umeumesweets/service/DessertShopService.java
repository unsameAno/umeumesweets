package com.umeume.umeumesweets.service;

import com.umeume.umeumesweets.entity.DessertShop;
import com.umeume.umeumesweets.repository.DessertShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DessertShopService {

    private final DessertShopRepository shopRepository;
    private final ImageUploadService imageUploadService; // ✅ Cloudinary 업로드용 서비스 주입

    // ✅ 가게 등록
    public void createShop(DessertShop shop, MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = imageUploadService.uploadImage(imageFile); // ✅ Cloudinary 업로드
                shop.setImageUrl(imageUrl);
            }
            shopRepository.save(shop);
        } catch (IOException e) {
            throw new RuntimeException("가게 이미지 업로드 실패", e);
        }
    }

    // ✅ 가게 수정
    public void updateShop(Long id, DessertShop updated, MultipartFile imageFile) {
        DessertShop existing = findById(id);
        existing.setShopName(updated.getShopName());
        existing.setPhone(updated.getPhone());
        existing.setZipcode(updated.getZipcode());
        existing.setAddress(updated.getAddress());
        existing.setDetailAddress(updated.getDetailAddress());
        existing.setDescription(updated.getDescription());

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = imageUploadService.uploadImage(imageFile); // ✅ Cloudinary 업로드
                existing.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류 발생", e);
            }
        }

        shopRepository.save(existing);
    }

    // 가게 리스트 조회 (검색 포함)
    public Page<DessertShop> getShops(Pageable pageable, String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return shopRepository.findByShopNameContaining(keyword, pageable);
        } else {
            return shopRepository.findAll(pageable);
        }
    }

    // 가게 단건 조회
    public DessertShop findById(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("가게가 존재하지 않습니다."));
    }

    // 가게 삭제
    public void deleteById(Long id) {
        shopRepository.deleteById(id);
    }

    // 전체 조회 (리스트 등)
    public List<DessertShop> getAll() {
        return shopRepository.findAll();
    }
}