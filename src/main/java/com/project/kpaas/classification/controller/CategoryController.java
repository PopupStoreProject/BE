package com.project.kpaas.classification.controller;

import com.project.kpaas.classification.service.CategoryService;
import com.project.kpaas.global.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<MessageResponseDto> categoryInitialSettingAdd(){
        return categoryService.addCategoryInitialSetting();
    }
}
