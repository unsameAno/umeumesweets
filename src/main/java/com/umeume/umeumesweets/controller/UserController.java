package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    // 🔸 회원정보 페이지 (수정 겸용)
    @GetMapping("/profile")
    public String showProfilePage(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        // 전화번호 분해
        String[] phoneParts = loginUser.getPhone().split("-");
        String phone1 = phoneParts.length > 0 ? phoneParts[0] : "";
        String phone2 = phoneParts.length > 1 ? phoneParts[1] : "";
        String phone3 = phoneParts.length > 2 ? phoneParts[2] : "";

        model.addAttribute("user", loginUser);
        model.addAttribute("phone1", phone1);
        model.addAttribute("phone2", phone2);
        model.addAttribute("phone3", phone3);

        return "user/profile"; // profile.html로 이동
    }

    // 🔸 회원정보 수정 처리
    @PostMapping("/profile")
    public String updateProfile(@RequestParam String username,
                                @RequestParam(required = false) String password,
                                @RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String phone1,
                                @RequestParam String phone2,
                                @RequestParam String phone3,
                                @RequestParam String address,
                                @RequestParam(required = false) String detailAddress,
                                HttpSession session,
                                Model model) {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "사용자 정보가 없습니다.");
            return "user/profile";
        }

        User user = optionalUser.get();

        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(password); // ※ 실제 서비스에서는 BCrypt로 암호화 권장
        }

        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone1 + "-" + phone2 + "-" + phone3);
        user.setAddress(address + " " + (detailAddress != null ? detailAddress : ""));

        userRepository.save(user);
        session.setAttribute("loginUser", user); // 세션 업데이트

        return "redirect:/user/profile"; // 자기 페이지로 리다이렉트
    }
}
