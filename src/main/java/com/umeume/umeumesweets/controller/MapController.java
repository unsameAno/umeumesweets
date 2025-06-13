package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.config.KakaoMapConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

    private final KakaoMapConfig kakaoMapConfig;

    public MapController(KakaoMapConfig kakaoMapConfig) {
        this.kakaoMapConfig = kakaoMapConfig;
    }

    @GetMapping("/map")
    public String mapPage(Model model) {
        model.addAttribute("kakaoApiKey", kakaoMapConfig.getApiKey());
        return "map-page"; // ← 사용할 Thymeleaf 템플릿명
    }
}
