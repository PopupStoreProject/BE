package com.project.kpaas.popup.controller;

import com.project.kpaas.global.security.UserDetailsImpl;
import com.project.kpaas.popup.dto.MessageResponseDto;
import com.project.kpaas.popup.dto.PopupRequestDto;
import com.project.kpaas.popup.dto.PopupResponseDto;
import com.project.kpaas.popup.service.PopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.project.kpaas.user.entity.User;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PopupController {

    private final PopupService popupService;

    @PostMapping("/popup")
    public ResponseEntity<MessageResponseDto> popupAdd(@RequestBody PopupRequestDto popupRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return popupService.addPopup(popupRequestDto, userDetails);
    }

    @GetMapping("/popups")
    public ResponseEntity<List<PopupResponseDto>> allPopupGet() {
        return popupService.getAllPopups();
    }

    @GetMapping("/popup/{id}")
    public ResponseEntity<PopupResponseDto> popupGet(@PathVariable Long id) {
        return popupService.getPopup(id);
    }


}
