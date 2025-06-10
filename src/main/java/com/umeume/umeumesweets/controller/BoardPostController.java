package com.umeume.umeumesweets.controller;

import com.umeume.umeumesweets.entity.BoardComment;
import com.umeume.umeumesweets.entity.BoardPost;
import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.service.BoardPostService;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardPostController {

    private final BoardPostService boardPostService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 게시글 목록 페이지
     */
    @GetMapping
    public String list(@RequestParam(value = "category", required = false) String category,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       Model model) {
        Page<BoardPost> postPage = boardPostService.getPosts(category, page, 50);
        model.addAttribute("postPage", postPage);
        model.addAttribute("category", category);
        return "board/board-list";
    }

    /**
     * 게시글 작성 폼 페이지
     */
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("post", new BoardPost());
        return "board/board-form";
    }

    /**
     * 게시글 등록 처리
     */
    @PostMapping
    public String submit(@ModelAttribute BoardPost post,
                         @SessionAttribute(value = "loginUser", required = false) User loginUser,
                         Model model) {
        if (loginUser != null) {
            post.setUser(loginUser);
        } else {
            String rawPassword = post.getGuestPassword();
            if (rawPassword == null || rawPassword.isBlank()) {
                model.addAttribute("error", "비회원은 비밀번호를 입력해야 합니다.");
                return "board/board-form";
            }
            String hashed = passwordEncoder.encode(rawPassword);
            post.setGuestPassword(hashed);
        }

        boardPostService.save(post);
        return "redirect:/board";
    }

    /**
     * 게시글 상세 페이지 (댓글 포함)
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         @SessionAttribute(value = "loginUser", required = false) User loginUser,
                         Model model) {
        BoardPost post = boardPostService.getPostById(id);
        model.addAttribute("post", post);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("newComment", new BoardComment());

        // 본인 여부 판단
        boolean isAuthor = loginUser != null && post.getUser() != null && loginUser.getId().equals(post.getUser().getId());
        model.addAttribute("isAuthor", isAuthor);

        // 댓글 삭제 가능 여부 맵 생성
        Map<Long, Boolean> canDeleteMap = new HashMap<>();
        for (BoardComment comment : post.getComments()) {
            boolean canDelete = loginUser != null && comment.getUser() != null && loginUser.getId().equals(comment.getUser().getId());
            canDeleteMap.put(comment.getId(), canDelete);
        }
        model.addAttribute("canDeleteMap", canDeleteMap);

        return "board/board-detail";
    }

    /**
     * 댓글 등록 처리
     */
    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @ModelAttribute BoardComment newComment,
                             @SessionAttribute(value = "loginUser", required = false) User loginUser) {
        newComment.setId(null);
        boardPostService.addComment(id, newComment, loginUser);
        return "redirect:/board/" + id;
    }

    /**
     * 댓글 삭제 처리
     */
    @PostMapping("/{id}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long id,
                                @PathVariable Long commentId,
                                @SessionAttribute(value = "loginUser", required = false) User loginUser) {
        boardPostService.deleteComment(commentId, loginUser);
        return "redirect:/board/" + id;
    }

    /**
     * 게시글 삭제 처리
     */
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

    /**
     * 게시글 수정 폼 페이지
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           @SessionAttribute(value = "loginUser", required = false) User loginUser,
                           Model model) {
        BoardPost post = boardPostService.getPostById(id);
        if (!boardPostService.canEdit(post, loginUser)) {
            return "redirect:/board/" + id;
        }
        model.addAttribute("post", post);
        return "board/board-form";
    }

    /**
     * 게시글 수정 처리
     */
    @PostMapping("/{id}/edit")
    public String editSubmit(@PathVariable Long id,
                            @ModelAttribute BoardPost updatedPost,
                            @SessionAttribute(value = "loginUser", required = false) User loginUser,
                            @RequestParam(value = "password", required = false) String password,
                            Model model) {

        // 🔍 실제 form에서 값 잘 넘어오는지 확인
        System.out.println("업데이트 요청 제목: " + updatedPost.getTitle());
        System.out.println("업데이트 요청 내용: " + updatedPost.getContent());
        System.out.println("업데이트 요청 비밀번호: " + password);

        boolean success = boardPostService.updatePost(id, updatedPost, loginUser, password);
        if (!success) {
            model.addAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/board/" + id;
        }
        return "redirect:/board/" + id;
    }
    /**
     * (예비용) 게시글 상세 보기 - URL 실수 방지용 백업 라우팅
     */
    @GetMapping("/board/{id}")
    public String viewPostDetail(@PathVariable Long id,
                                 @SessionAttribute(value = "loginUser", required = false) User loginUser,
                                 Model model) {
        BoardPost post = boardPostService.getPostById(id);
        if (post == null) return "redirect:/board";

        List<BoardComment> comments = boardPostService.getComments(post);
        Map<Long, Boolean> canDeleteMap = new HashMap<>();
        for (BoardComment comment : comments) {
            boolean canDelete = loginUser != null && comment.getUser() != null && comment.getUser().equals(loginUser);
            canDeleteMap.put(comment.getId(), canDelete);
        }

        model.addAttribute("post", post);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("canDeleteMap", canDeleteMap);
        return "board/board-detail";
    }
}
