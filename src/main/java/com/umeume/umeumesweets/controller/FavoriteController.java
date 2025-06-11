package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.service.FavoriteService;
import com.umeume.umeumesweets.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService; // 유저 조회용

    @PostMapping("/favorites/toggle/{productId}")
@ResponseBody
public ResponseEntity<?> toggleFavorite(@PathVariable Long productId, HttpSession session) {
    User user = (User) session.getAttribute("loginUser");
    if (user == null) {
        return ResponseEntity.status(401).body("로그인이 필요합니다");
    }

    Long userId = user.getId();
    boolean isLiked = favoriteService.toggleFavorite(userId, productId);
    return ResponseEntity.ok(Map.of("liked", isLiked));
}

@GetMapping("/favorites")
public String favoriteList(Model model, HttpSession session) {
    User user = (User) session.getAttribute("loginUser");
    if (user == null) return "redirect:/login";

    model.addAttribute("favorites", favoriteService.getFavoriteProducts(user.getId()));
    return "favorite-list";
}

}
