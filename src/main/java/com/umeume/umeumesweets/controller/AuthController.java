package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    // 로그인 페이지 GET
    @GetMapping("/login")
    public String showLoginPage() {
        log.info("여기야!!!!!!!");
        return "login";
    }

    // 회원가입 페이지 GET
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // 아이디 찾기 페이지
    @GetMapping("/find-id")
    public String showFindIdPage() {
        return "find-id";
    }

    // 비밀번호 찾기 페이지
    @GetMapping("/find-pw")
    public String showFindPwPage() {
        return "find-pw";
    }

    // 회원가입 POST 처리
    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user, Model model) {
        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "이미 존재하는 아이디입니다.");
            return "register";
        }
        userRepository.save(user); // 비밀번호 암호화는 추후 추가
        return "redirect:/login";
    }

    // 로그인 POST 처리
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

    Optional<User> optionalUser = userRepository.findByUsername(username);

    if (optionalUser.isEmpty() || !optionalUser.get().getPassword().equals(password)) {
        model.addAttribute("loginError", "아이디 또는 비밀번호가 틀렸습니다.");
        return "login";
    }

    session.setAttribute("loginUser", optionalUser.get());
    return "redirect:/";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
    session.invalidate(); // 세션 전체 초기화 (또는 session.removeAttribute("loginUser")도 가능)
    return "redirect:/"; // 메인 페이지로 리다이렉트
    }

}
