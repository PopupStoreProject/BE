package com.project.kpaas.popup.repository;

import com.project.kpaas.popup.entity.Popupstore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PopupRepository extends JpaRepository<Popupstore, Long> {

    Optional<Popupstore> findById(Long popupId);
}
