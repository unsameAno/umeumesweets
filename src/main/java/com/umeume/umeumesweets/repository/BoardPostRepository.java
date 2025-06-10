package com.umeume.umeumesweets.repository;

import com.umeume.umeumesweets.entity.BoardPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {

    // 🔥 이거 추가해주이소!
    Page<BoardPost> findByCategory(String category, Pageable pageable);
}
