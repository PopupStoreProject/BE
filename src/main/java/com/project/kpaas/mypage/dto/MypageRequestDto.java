package com.project.kpaas.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@NoArgsConstructor
public class MypageRequestDto {
    @NotNull(message = "사용자명은 필수 값입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,10}", message = "아이디는 소문자 4-10자 이내 입니다.")
    private String username;

    @Nullable
    private String[] categoryPreference;

    @Nullable
    private String userImage;

}
