package com.project.kpaas.mypage.controller;

import com.project.kpaas.mypage.dto.MypageRequestDto;
import com.project.kpaas.mypage.dto.MypageResponseDto;
import com.project.kpaas.global.dto.MessageResponseDto;
import com.project.kpaas.global.security.UserDetailsImpl;
import com.project.kpaas.mypage.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/mypage")
    public MypageResponseDto myInfoGet(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.getMyInfo(userDetails.getUser());
    }

    @PutMapping("/mypage/edit")
    public MessageResponseDto myInfoUpdate(MypageRequestDto mypageRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.updateMyInfo(mypageRequestDto, userDetails.getUser());
    }

    @PutMapping("/bookmark/{id}")
    public ResponseEntity<MessageResponseDto> bookmarkRegister(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.pushLike(id, userDetails.getUser());
    }

}
