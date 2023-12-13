package com.project.kpaas.classification.repository;

import com.project.kpaas.classification.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    List<Hashtag> findAllByPopupstoreId(Long id);
    void deleteByPopupstoreId(Long id);
}
