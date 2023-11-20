package com.project.kpaas.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter @Setter
public class SignupRequestDto {
    @NotNull(message = "사용자명은 필수 값입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,10}", message = "아이디는 소문자 4-10자 이내 입니다.")
    private String username;

    @NotNull(message = "이메일은 필수 값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9\\d`~!@#$%^&*()-_=+]{8,24}$", message = "이메일은 대소문자 및 특수문자 포함 8-24자 입니다.")
    private String email;

    @NotNull(message = "비밀번호는 필수 값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9\\d`~!@#$%^&*()-_=+]{8,24}$", message = "비밀번호는 대소문자 및 특수문자 포함 8-24자 이내 입니다.")
    private String password;

    @Nullable
    private List<String> categoryPreference;

    private boolean admin = false;
    private String adminToken = "";
}
