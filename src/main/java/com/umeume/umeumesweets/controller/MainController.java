package com.umeume.umeumesweets.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

import java.util.*;

@Controller
public class MainController {

@GetMapping("/")
public String index(HttpSession session, Model model) {
    Object loginUser = session.getAttribute("loginUser");
    model.addAttribute("loginUser", loginUser);

    // 임시 디저트 더미 데이터 추가 (필수)
    List<Map<String, String>> desserts = new ArrayList<>();
    Map<String, String> d = new HashMap<>();
    d.put("name", "초코 케이크");
    d.put("imageUrl", "/images/sample.jpg");
    d.put("cafeName", "디저트카페");
    d.put("price", "4500");
    desserts.add(d);
    model.addAttribute("desserts", desserts);

    return "index";
}
}
