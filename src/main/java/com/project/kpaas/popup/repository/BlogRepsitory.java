package com.project.kpaas.popup.repository;

import com.project.kpaas.popup.entity.BlogReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogRepsitory extends JpaRepository<BlogReview, Long> {
    List<BlogReview> findAllByPopupstoreId(Long id);
}
