package com.umeume.umeumesweets.service;

import com.umeume.umeumesweets.entity.BoardComment;
import com.umeume.umeumesweets.entity.BoardPost;
import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.repository.BoardCommentRepository;
import com.umeume.umeumesweets.repository.BoardPostRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardPostService {

    private final BoardPostRepository boardPostRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<BoardPost> getPosts(String category, int page, int size) {
        if (category == null || category.isBlank()) {
            return boardPostRepository.findAll(PageRequest.of(page, size));
        }
        return boardPostRepository.findByCategory(category, PageRequest.of(page, size));
    }

    public BoardPost getPostById(Long id) {
        return boardPostRepository.findById(id).orElse(null);
    }

    public BoardPost findById(Long id) {
    return boardPostRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID=" + id));
}

public void saveComment(BoardComment comment) {
    boardCommentRepository.save(comment);
}

    public void save(BoardPost post) {
        boardPostRepository.save(post);
    }

    public boolean checkGuestPassword(String raw, String hashed) {
        return passwordEncoder.matches(raw, hashed);
    }

    public boolean deleteComment(Long commentId, User loginUser, String password) {
        Optional<BoardComment> optional = boardCommentRepository.findById(commentId);
        if (optional.isEmpty()) return false;

        BoardComment comment = optional.get();

        // 로그인 유저의 댓글일 경우
        if (comment.getUser() != null &&
            loginUser != null &&
            comment.getUser().getId().equals(loginUser.getId())) {
            boardCommentRepository.delete(comment);
            return true;
        }

        // 비회원 댓글일 경우
        if (comment.getUser() == null &&
            password != null &&
            checkGuestPassword(password, comment.getGuestPassword())) {
            boardCommentRepository.delete(comment);
            return true;
        }

        return false;
    }

    public List<BoardComment> getComments(BoardPost post) {
            return boardCommentRepository.findByPostOrderByCreatedAtAsc(post);
        }

        public boolean deletePost(Long postId, User loginUser, String password) {
        BoardPost post = getPostById(postId);
        if (post == null) return false;

        // 로그인 유저가 작성자일 경우
        if (post.getUser() != null && loginUser != null &&
            post.getUser().getId().equals(loginUser.getId())) {
            boardPostRepository.delete(post);
            return true;
        }

        // 비회원일 경우 비밀번호 일치 시 삭제
        if (post.getUser() == null && password != null &&
            passwordEncoder.matches(password, post.getGuestPassword())) {
            boardPostRepository.delete(post);
            return true;
        }

        return false;
    }

    public boolean canEdit(BoardPost post, User loginUser) {
    if (post == null || loginUser == null || post.getUser() == null) return false;

    Long postUserId = post.getUser().getId();
    Long loginUserId = loginUser.getId();

    return postUserId != null && postUserId.equals(loginUserId);
}

    public boolean updatePost(Long id, BoardPost updatedPost, User loginUser, String password, HttpSession session) {
    BoardPost original = getPostById(id);
    if (original == null) return false;

    boolean isLoginUserAuthor = original.getUser() != null && loginUser != null
        && original.getUser().getId().equals(loginUser.getId());

    // 여기에서 세션 인증도 추가!
    Long guestEditAccess = (Long) session.getAttribute("guestEditAccess");
    boolean isGuestAuthor = original.getUser() == null &&
                            guestEditAccess != null &&
                            guestEditAccess.equals(id);

    if (!isLoginUserAuthor && !isGuestAuthor) {
        return false;
    }

    // 수정 진행
    original.setTitle(updatedPost.getTitle());
    original.setCategory(updatedPost.getCategory());
    original.setContent(updatedPost.getContent());

    boardPostRepository.save(original);
    return true;
}


}
