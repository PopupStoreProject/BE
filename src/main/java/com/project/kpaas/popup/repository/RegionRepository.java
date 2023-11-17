package com.project.kpaas.popup.repository;

import com.project.kpaas.popup.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findById(Long regionId);
}
