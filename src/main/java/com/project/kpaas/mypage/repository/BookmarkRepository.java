package com.project.kpaas.mypage.repository;

import com.project.kpaas.mypage.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByPopupstoreIdAndUserId(Long popupstoreId, Long userId);

    List<Bookmark> findAllByUserId(Long userId);
    void deleteByPopupstoreId(Long popupstoreId);
}
