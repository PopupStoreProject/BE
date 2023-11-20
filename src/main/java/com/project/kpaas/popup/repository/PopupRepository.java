package com.project.kpaas.popup.repository;

import com.project.kpaas.classification.entity.Category;
import com.project.kpaas.popup.entity.Popupstore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PopupRepository extends JpaRepository<Popupstore, Long> {

    Optional<Popupstore> findById(Long popupId);

    List<Popupstore> findAllByCategory(Category category);

    List<Popupstore> findAllByUserId(Long usesrId);
}
