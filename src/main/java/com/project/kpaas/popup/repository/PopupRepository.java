package com.project.kpaas.popup.repository;

import com.project.kpaas.classification.entity.Category;
import com.project.kpaas.popup.entity.Popupstore;
import com.project.kpaas.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PopupRepository extends JpaRepository<Popupstore, Long> {

    Optional<Popupstore> findById(Long popupId);
    Optional<Popupstore> findByIdAndUser(Long id, User user);
    List<Popupstore> findAllByCategory(Category category);
    List<Popupstore> findAllByUserId(Long usesrId);

    void deleteById(Long id);
}
