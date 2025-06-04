package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.repository.UserRepository;

import jakarta.annotation.PostConstruct;
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

    @GetMapping("/profile")
    public String showProfilePage(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        model.addAttribute("user", loginUser);

        String phone = loginUser.getPhone();
        if (phone != null && phone.length() == 11) {
            String formatted = phone.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
            model.addAttribute("formattedPhone", formatted);
        } else {
            model.addAttribute("formattedPhone", phone);
        }

        return "user/profile";
    }

    // 🔸 프로필 수정 처리
    @PostMapping("/profile")
    public String updateProfile(@RequestParam String username,
                                @RequestParam(required = false) String password,
                                @RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String phone,
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

        // 🔸 비밀번호 유효성 검사 추가
        if (password != null && !password.trim().isEmpty()) {
            if (password.length() < 8) {
                model.addAttribute("error", "비밀번호는 최소 8자 이상이어야 합니다.");
                model.addAttribute("user", user);
                model.addAttribute("formattedPhone", phone);
                return "user/profile";
            }
            user.setPassword(password);
        }

        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone.replaceAll("-", ""));
        user.setAddress(address + " " + (detailAddress != null ? detailAddress : ""));

        userRepository.save(user);
        session.setAttribute("loginUser", user);

        return "redirect:/user/profile";
    }

    @GetMapping("/delete")
    public String deleteUser(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null) {
            userRepository.deleteById(loginUser.getId());
            session.invalidate();
        }
        return "redirect:/login";
    }
}
