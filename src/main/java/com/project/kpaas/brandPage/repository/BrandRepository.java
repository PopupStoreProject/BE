package com.project.kpaas.brandPage.repository;

import com.project.kpaas.brandPage.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByUserId(Long userId);
}
