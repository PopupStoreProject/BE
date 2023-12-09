package com.project.kpaas.popup.repository;

import com.project.kpaas.popup.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Images, Long> {
    List<Images> findAllByPopupstoreId(Long popupId);
}
