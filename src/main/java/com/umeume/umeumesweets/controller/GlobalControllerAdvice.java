package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("loginUser")
    public User loginUser(HttpSession session) {
        return (User) session.getAttribute("loginUser");
    }
}
