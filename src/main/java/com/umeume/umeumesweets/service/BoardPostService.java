package com.umeume.umeumesweets.service;

import com.umeume.umeumesweets.entity.BoardComment;
import com.umeume.umeumesweets.entity.BoardPost;
import com.umeume.umeumesweets.entity.User;
import com.umeume.umeumesweets.repository.BoardCommentRepository;
import com.umeume.umeumesweets.repository.BoardPostRepository;
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

    /**
     * 게시글 목록 조회 (카테고리별 + 페이지네이션)
     */
    public Page<BoardPost> getPosts(String category, int page, int size) {
        if (category == null || category.isBlank()) {
            return boardPostRepository.findAll(PageRequest.of(page, size));
        }
        return boardPostRepository.findByCategory(category, PageRequest.of(page, size));
    }

    /**
     * 게시글 단건 조회
     */
    public BoardPost getPostById(Long id) {
        return boardPostRepository.findById(id).orElse(null);
    }

    /**
     * 게시글 저장 (등록 시 사용)
     */
    public void save(BoardPost post) {
        boardPostRepository.save(post);
    }

    /**
     * 비회원용 비밀번호 일치 여부 확인
     */
    public boolean checkGuestPassword(BoardPost post, String rawPassword) {
        return passwordEncoder.matches(rawPassword, post.getGuestPassword());
    }

    /**
     * 댓글 저장 (회원/비회원 공통)
     */
    public void addComment(Long postId, BoardComment comment, User loginUser) {
    BoardPost post = getPostById(postId);
    if (post != null) {
        comment.setId(null); // 무조건 새 댓글로 인식되게 만듬
        comment.setPost(post);
        if (loginUser != null) {
            comment.setUser(loginUser); // 🔹 로그인 사용자 연결
        }
        // 비회원은 guestName, guestPassword가 form으로 넘어옴
        boardCommentRepository.save(comment);
    }
}

    /**
     * 댓글 삭제 (회원만 해당 댓글의 작성자일 경우 허용)
     */
    public void deleteComment(Long commentId, User loginUser) {
        Optional<BoardComment> optional = boardCommentRepository.findById(commentId);
        optional.ifPresent(comment -> {
            if (comment.getUser() != null && comment.getUser().equals(loginUser)) {
                boardCommentRepository.delete(comment);
            }
        });
    }

    /**
     * 특정 게시글의 전체 댓글 목록 조회 (작성일 기준 정렬)
     */
    public List<BoardComment> getComments(BoardPost post) {
        return boardCommentRepository.findByPostOrderByCreatedAtAsc(post);
    }

    /**
     * 게시글 삭제
     * - 회원: 본인인 경우만 삭제 가능
     * - 비회원: 비밀번호 일치 시 삭제 가능
     */
    public boolean deletePost(Long postId, User loginUser, String password) {
        BoardPost post = getPostById(postId);
        if (post == null) return false;

        if (post.getUser() != null && post.getUser().equals(loginUser)) {
            boardPostRepository.delete(post);
            return true;
        }

        if (post.getUser() == null && checkGuestPassword(post, password)) {
            boardPostRepository.delete(post);
            return true;
        }

        return false;
    }

    /**
     * 게시글 수정 권한 여부 확인
     */
    public boolean canEdit(BoardPost post, User loginUser) {
    if (post == null || loginUser == null || post.getUser() == null) return false;
    return post.getUser().getId().equals(loginUser.getId());
    }

    /**
     * 게시글 수정 처리
     * - 회원: 본인일 경우만 허용
     * - 비회원: 비밀번호 일치 시 허용
     */
    public boolean updatePost(Long id, BoardPost updatedPost, User loginUser, String password) {
        BoardPost original = getPostById(id);
        if (original == null) return false;

        boolean canUpdate =
            (original.getUser() != null && loginUser != null && original.getUser().getId().equals(loginUser.getId())) ||
            (original.getUser() == null && checkGuestPassword(original, password));

        if (canUpdate) {
            original.setTitle(updatedPost.getTitle());
            original.setCategory(updatedPost.getCategory());
            original.setContent(updatedPost.getContent());
            boardPostRepository.save(original);
            return true;
        }

        return false;
    }
}
