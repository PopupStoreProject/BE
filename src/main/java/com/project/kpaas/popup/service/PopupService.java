package com.project.kpaas.popup.service;

import com.project.kpaas.global.exception.CustomException;
import com.project.kpaas.global.exception.ErrorCode;
import com.project.kpaas.global.security.UserDetailsImpl;
import com.project.kpaas.popup.dto.MessageResponseDto;
import com.project.kpaas.popup.dto.PopupRequestDto;
import com.project.kpaas.popup.dto.PopupResponseDto;
import com.project.kpaas.popup.entity.PopupStore;
import com.project.kpaas.popup.repository.PopupRepository;
import com.project.kpaas.user.entity.UserRole;
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
public class PopupService {

    private final PopupRepository popupRepository;

    @Transactional
    public ResponseEntity<MessageResponseDto> addPopup(PopupRequestDto popupRequestDto, UserDetailsImpl userDetails) {

        if (userDetails.getUser().getRole() == UserRole.USER) {
            throw new CustomException(ErrorCode.AUTHORIZATION);
        }

        PopupStore newPopupStore = PopupStore.from(popupRequestDto, userDetails.getUser());

        popupRepository.save(newPopupStore);
        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "게시글 등록 완료", newPopupStore.getId()));
    }

    @Transactional
    public ResponseEntity<List<PopupResponseDto>> getAllPopups() {
        List<PopupStore> popupStores = popupRepository.findAll();
        List<PopupResponseDto> popupResponseDto = new ArrayList<>();

        for (PopupStore popupStore : popupStores) {
            popupResponseDto.add(PopupResponseDto.of(popupStore));
        }
        return ResponseEntity.ok().body(popupResponseDto);
    }

    @Transactional
    public ResponseEntity<PopupResponseDto> getPopup(Long id) {
        Optional<PopupStore> popupStore = popupRepository.findById(id);
        if (popupStore.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_POPUP);
        }
        return ResponseEntity.ok().body(PopupResponseDto.of(popupStore.get()));
    }


}
