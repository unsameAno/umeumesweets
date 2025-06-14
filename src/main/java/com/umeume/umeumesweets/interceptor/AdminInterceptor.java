package com.umeume.umeumesweets.interceptor;

import com.umeume.umeumesweets.entity.User;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("/login?message=admin_only");
            return false;
        }

        Object userObj = session.getAttribute("loginUser");
        if (userObj == null || !(userObj instanceof User)) {
            response.sendRedirect("/login?message=admin_only");
            return false;
        }

        User user = (User) userObj;
        if (!"admin".equals(user.getUsername())) {
            response.sendRedirect("/login?message=admin_only");
            return false;
        }

        return true;
    }
}
