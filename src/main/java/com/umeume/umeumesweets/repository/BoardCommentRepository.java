package com.umeume.umeumesweets.repository;

import com.umeume.umeumesweets.entity.BoardComment;
import com.umeume.umeumesweets.entity.BoardPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    List<BoardComment> findByPostOrderByCreatedAtAsc(BoardPost post);
}
