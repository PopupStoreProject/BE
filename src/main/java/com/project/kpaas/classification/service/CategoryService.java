package com.project.kpaas.classification.service;

import com.project.kpaas.classification.entity.Category;
import com.project.kpaas.classification.repository.CategoryRepository;

import com.project.kpaas.global.dto.SuccessResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public ResponseEntity<SuccessResponseDto> addCategoryInitialSetting (){
        // 초기 카테고리 생성용 임시 메서드 -> 추후 팝업 관리자만 카테고리 생성, 수정 가능하도록 변경
        String[] categoryNames = {
                "패션", "뷰티", "음식", "운동",
                "아트", "엔터", "테크", "환경", "기타"
        };

        Arrays.stream(categoryNames)
                .map(Category::from)
                .forEach(categoryRepository::save);

        return ResponseEntity.ok().body(SuccessResponseDto.of("카테고리 등록 완료", HttpStatus.OK));
    }
}
