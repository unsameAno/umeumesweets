package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.BoardComment;
import com.umeume.umeumesweets.entity.BoardPost;
import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.service.BoardPostService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardPostController {

    private final BoardPostService boardPostService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String list(@RequestParam(value = "category", required = false) String category,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       Model model) {
        Page<BoardPost> postPage = boardPostService.getPosts(category, page, 50);
        model.addAttribute("postPage", postPage);
        model.addAttribute("category", category);
        return "board/board-list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("post", new BoardPost());
        return "board/board-form";
    }

    @PostMapping
    public String submit(@ModelAttribute BoardPost post,
                        @RequestParam(value = "password", required = false) String password,
                        @SessionAttribute(value = "loginUser", required = false) User loginUser,
                        Model model) {
        if (loginUser != null) {
            post.setUser(loginUser);
        } else {
            if (password == null || password.isBlank()) {
                model.addAttribute("error", "비회원은 비밀번호를 입력해야 합니다.");
                return "board/board-form";
            }
            post.setGuestPassword(passwordEncoder.encode(password));
        }

        boardPostService.save(post);
        return "redirect:/board";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         @SessionAttribute(value = "loginUser", required = false) User loginUser,
                         Model model) {
        BoardPost post = boardPostService.getPostById(id);
        model.addAttribute("post", post);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("newComment", new BoardComment());

        boolean isAuthor = loginUser != null && post.getUser() != null &&
                           loginUser.getId().equals(post.getUser().getId());
        model.addAttribute("isAuthor", isAuthor);

        Map<Long, Boolean> canDeleteMap = new HashMap<>();
        for (BoardComment comment : post.getComments()) {
            boolean canDelete = loginUser != null &&
                                comment.getUser() != null &&
                                loginUser.getId().equals(comment.getUser().getId());
            canDeleteMap.put(comment.getId(), canDelete);
        }
        model.addAttribute("canDeleteMap", canDeleteMap);

        return "board/board-detail";
    }

    @PostMapping("/{id}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long id,
                                @PathVariable Long commentId,
                                @RequestParam(value = "password", required = false) String password,
                                @SessionAttribute(value = "loginUser", required = false) User loginUser,
                                RedirectAttributes redirectAttributes) {

        boolean success = boardPostService.deleteComment(commentId, loginUser, password);
        if (!success) {
            redirectAttributes.addFlashAttribute("errorMessage", "댓글 삭제 권한이 없습니다.");
        }
        return "redirect:/board/" + id;
    }

    @PostMapping("/{id}/delete")
        public String deletePost(@PathVariable Long id,
                                @RequestParam(value = "password", required = false) String password,
                                @SessionAttribute(value = "loginUser", required = false) User loginUser,
                                Model model) {
            boolean success = boardPostService.deletePost(id, loginUser, password);
            if (!success) {
                model.addAttribute("error", "삭제 권한이 없습니다.");
                return "redirect:/board/" + id;
            }
    return "redirect:/board";
}

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                        @SessionAttribute(value = "loginUser", required = false) User loginUser,
                        HttpSession session,
                        Model model) {
        BoardPost post = boardPostService.getPostById(id);

        // 회원 인증 여부
        boolean isMemberAuthor = post.getUser() != null &&
                                loginUser != null &&
                                post.getUser().getId().equals(loginUser.getId());

        // 비회원 인증 여부 (세션 기반)
        Long guestEditAccess = (Long) session.getAttribute("guestEditAccess");
        boolean isGuestAuthor = post.getUser() == null &&
                                guestEditAccess != null &&
                                guestEditAccess.equals(id);

        if (!isMemberAuthor && !isGuestAuthor) {
            return "redirect:/board/" + id;
        }

        model.addAttribute("post", post);
        return "board/board-form";
    }

    @PostMapping("/{id}/edit")
    public String editSubmit(@PathVariable Long id,
                            @ModelAttribute BoardPost updatedPost,
                            @SessionAttribute(value = "loginUser", required = false) User loginUser,
                            @RequestParam(value = "password", required = false) String password,
                            Model model,
                            HttpSession session) {
        boolean success = boardPostService.updatePost(id, updatedPost, loginUser, password, session);
        if (!success) {
            model.addAttribute("error", "수정 권한이 없습니다.");
            model.addAttribute("post", boardPostService.getPostById(id));
            return "board/board-form";
        }
        return "redirect:/board/" + id;
    }

    @PostMapping("/{id}/edit-auth")
    public String guestEditAuth(@PathVariable Long id,
                                @RequestParam String password,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        BoardPost post = boardPostService.findById(id);

        if (post.getUser() != null) {
            return "redirect:/board/" + id; // 로그인 유저 게시글은 여기 안 들어오게
        }

        if (!passwordEncoder.matches(password, post.getGuestPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "redirect:/board/" + id;
        }

        session.setAttribute("guestEditAccess", id); // 임시 인증용 세션 저장
        return "redirect:/board/" + id + "/edit";
    }


    @PostMapping("/{id}/comment")
public String addComment(@PathVariable Long id,
                         @RequestParam String content,
                         @RequestParam(required = false) String guestName,
                         @RequestParam(required = false) String guestPassword,
                         @SessionAttribute(value = "loginUser", required = false) User loginUser) {

    BoardPost post = boardPostService.getPostById(id);
    if (post == null || content == null || content.isBlank()) {
        return "redirect:/board/" + id;
    }

    BoardComment comment = new BoardComment();
    comment.setPost(post);
    comment.setContent(content);

    if (loginUser != null) {
        comment.setUser(loginUser);
    } else {
        comment.setGuestName(guestName);
        comment.setGuestPassword(passwordEncoder.encode(guestPassword)); // <-- 요거 핵심!!!
    }

    boardPostService.saveComment(comment);
    return "redirect:/board/" + id;
}
}
