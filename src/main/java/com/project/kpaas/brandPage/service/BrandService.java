package com.project.kpaas.brandPage.service;

import com.project.kpaas.brandPage.dto.BrandRequestDto;
import com.project.kpaas.brandPage.dto.BrandResponseDto;
import com.project.kpaas.brandPage.entity.Brand;
import com.project.kpaas.brandPage.repository.BrandRepository;
import com.project.kpaas.global.dto.MessageResponseDto;
import com.project.kpaas.global.exception.CustomException;
import com.project.kpaas.global.exception.ErrorCode;
import com.project.kpaas.global.security.UserDetailsImpl;
import com.project.kpaas.popup.entity.Popupstore;
import com.project.kpaas.popup.repository.PopupRepository;
import com.project.kpaas.user.entity.User;
import com.project.kpaas.user.entity.UserRole;
import com.project.kpaas.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final PopupRepository popupRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<MessageResponseDto> addBrandInfo(BrandRequestDto brandRequestDto, UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole() == UserRole.USER) {
            throw new CustomException(ErrorCode.AUTHORIZATION);
        }

        brandRepository.save(Brand.of(brandRequestDto, userDetails.getUser()));
        return ResponseEntity.ok().body(MessageResponseDto.of( "브랜드 정보 등록 완료", HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<BrandResponseDto> getBrandInfo(User user) {

        Optional<User> foundUser = userRepository.findById(user.getId());
        if(foundUser.isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        Optional<Brand> foundBrand = brandRepository.findByUserId(foundUser.get().getId());
        if (foundBrand.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_BRAND);
        }

        List<List<String>> targetPopupInfos = new ArrayList<>();
        List<Popupstore> registerdPopups = popupRepository.findAllByUserId(user.getId());
        for (Popupstore popupstore : registerdPopups) {
            List<String> targetPopupDetails = new ArrayList<>();
            targetPopupDetails.add(popupstore.getPopupName());
            targetPopupDetails.add(popupstore.getCategory().getCategoryName());
//            targetPopupDetails.add(popupstore.getImageUrl());
            targetPopupInfos.add(targetPopupDetails);
        }

        return ResponseEntity.ok().body(BrandResponseDto.of(foundBrand.get(), foundUser.get(), targetPopupInfos.toArray(Object[]::new)));
    }

    @Transactional
    public ResponseEntity<MessageResponseDto> updateBrandInfo(BrandRequestDto brandRequestDto, UserDetailsImpl userDetails) {
        Optional<Brand> foundBrand = brandRepository.findByUserId(userDetails.getUser().getId());
        if (foundBrand.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        foundBrand.get().update(brandRequestDto);
        return ResponseEntity.ok().body(MessageResponseDto.of("수정이 완료되었습니다.", HttpStatus.OK));
    }

}
