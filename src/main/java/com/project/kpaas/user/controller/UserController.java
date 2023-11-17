package com.project.kpaas.user.controller;

import com.project.kpaas.user.dto.LoginRequestDto;
import com.project.kpaas.global.dto.SuccessResponseDto;
import com.project.kpaas.user.dto.SignupRequestDto;
import com.project.kpaas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

}
