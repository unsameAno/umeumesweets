package com.umeume.umeumesweets.controller.admin;

import com.umeume.umeumesweets.entity.DessertShop;
import com.umeume.umeumesweets.repository.DessertShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/admin/shop")
public class DessertShopController {

    @Autowired
    private DessertShopRepository dessertShopRepository;

    private final String uploadDir;

    // 경로 초기화 (톰캣에서도 인식 가능한 방식으로!)
    public DessertShopController() throws IOException {
        this.uploadDir = new ClassPathResource("static/images/shop/").getFile().getAbsolutePath() + File.separator;
    }

    @GetMapping("/new")
    public String showShopForm(Model model) {
        model.addAttribute("shop", new DessertShop());
        return "admin/shop-form";
    }

    @PostMapping("/new")
    public String saveShop(@ModelAttribute DessertShop shop,
                           @RequestParam("image") MultipartFile imageFile) throws IOException {

        // ✅ 디렉토리 없으면 생성
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // ✅ 이미지 업로드
        if (!imageFile.isEmpty()) {
            String originalFilename = imageFile.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID() + extension;

            File destFile = new File(uploadDir + newFilename);
            imageFile.transferTo(destFile);

            shop.setImageUrl("/images/shop/" + newFilename);
        }

        dessertShopRepository.save(shop);
        return "redirect:/admin/shop/new?success";
    }
}
