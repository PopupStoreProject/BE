package com.project.kpaas.brandPage.service;

import com.project.kpaas.brandPage.dto.BrandRequestDto;
import com.project.kpaas.brandPage.entity.Brand;
import com.project.kpaas.brandPage.repository.BrandRepository;
import com.project.kpaas.global.dto.SuccessResponseDto;
import com.project.kpaas.global.exception.CustomException;
import com.project.kpaas.global.exception.ErrorCode;
import com.project.kpaas.global.security.UserDetailsImpl;
import com.project.kpaas.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    @Transactional
    public ResponseEntity<SuccessResponseDto> addBrandInfo(BrandRequestDto brandRequestDto, UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole() == UserRole.USER) {
            throw new CustomException(ErrorCode.AUTHORIZATION);
        }

        brandRepository.save(Brand.of(brandRequestDto, userDetails.getUser()));
        return ResponseEntity.ok().body(SuccessResponseDto.of( "브랜드 정보 등록 완료", HttpStatus.OK));
    }

}
