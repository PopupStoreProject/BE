package com.project.kpaas.mypage.controller;

import com.project.kpaas.global.dto.SuccessResponseDto;
import com.project.kpaas.global.security.UserDetailsImpl;
import com.project.kpaas.mypage.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MypageController {

    private final BookmarkService bookmarkService;

    @PutMapping("/bookmark/{id}")
    public ResponseEntity<SuccessResponseDto> createLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return bookmarkService.pushLike(id, userDetails.getUser());
    }
}
