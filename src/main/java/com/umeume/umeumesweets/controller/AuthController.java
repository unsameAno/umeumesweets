package com.umeume.umeumesweets.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/find-id")
    public String showFindIdPage() {
        return "find-id";
    }

    @GetMapping("/find-pw")
    public String showFindPwPage() {
        return "find-pw";
    }

    @PostMapping("/register")
    public String processRegister() {
    // TODO: 유효성 검사 + 저장 로직 (이후 구현)
    return "redirect:/login";
    }
}
