package com.umeume.umeumesweets.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploadService {

    private final Cloudinary cloudinary;

    public ImageUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

        // ✅ 업로드된 이미지 URL 콘솔에 출력
        String imageUrl = uploadResult.get("secure_url").toString();
        System.out.println("✅ 업로드된 Cloudinary 이미지 URL: " + imageUrl);

        return imageUrl;
    }
}
