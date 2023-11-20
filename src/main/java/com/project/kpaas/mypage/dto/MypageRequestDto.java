package com.project.kpaas.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter
@NoArgsConstructor
public class MypageRequestDto {
    private String username;

    @Nullable
    private List<String> categoryPreference;

}
