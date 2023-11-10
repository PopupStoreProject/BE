package com.project.kpaas.popup.repository;

import com.project.kpaas.popup.entity.PopupStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PopupRepository extends JpaRepository<PopupStore, Long> {

    Optional<PopupStore> findById(Long popupId);
}
