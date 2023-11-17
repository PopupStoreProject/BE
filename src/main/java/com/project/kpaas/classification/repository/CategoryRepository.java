package com.project.kpaas.classification.repository;

import com.project.kpaas.classification.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
