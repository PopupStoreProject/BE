package com.project.kpaas.mypage.repository;

import com.project.kpaas.mypage.entity.Bookmark;
import com.project.kpaas.mypage.entity.CategoryPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryPreferenceRepository extends JpaRepository<CategoryPreference, Long> {

    List<CategoryPreference> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
