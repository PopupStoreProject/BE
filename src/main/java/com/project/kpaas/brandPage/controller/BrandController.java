package com.project.kpaas.brandPage.controller;

import com.project.kpaas.brandPage.dto.BrandRequestDto;
import com.project.kpaas.brandPage.service.BrandService;
import com.project.kpaas.global.dto.SuccessResponseDto;
import com.project.kpaas.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping("/brand/mypage")
    public ResponseEntity<SuccessResponseDto> brandInfoAdd(@RequestBody BrandRequestDto brandRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return brandService.addBrandInfo(brandRequestDto, userDetails);
    }
}
