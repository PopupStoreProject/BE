package com.project.kpaas.brandPage.controller;

import com.project.kpaas.brandPage.dto.BrandRequestDto;
import com.project.kpaas.brandPage.dto.BrandResponseDto;
import com.project.kpaas.brandPage.service.BrandService;
import com.project.kpaas.global.dto.MessageResponseDto;
import com.project.kpaas.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping("/brand/mypage")
    public ResponseEntity<MessageResponseDto> brandInfoAdd(@RequestBody BrandRequestDto brandRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return brandService.addBrandInfo(brandRequestDto, userDetails);
    }

    @GetMapping("/brand/mypage")
    public ResponseEntity<BrandResponseDto> brandInfoGet(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return brandService.getBrandInfo(userDetails.getUser());
    }

    @PutMapping("/brand/mypage/edit")
    public ResponseEntity<MessageResponseDto> brandInfoUpdate(@RequestBody BrandRequestDto brandRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return brandService.updateBrandInfo(brandRequestDto, userDetails);
    }


}
