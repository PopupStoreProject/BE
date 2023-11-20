package com.project.kpaas.mypage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.project.kpaas.user.entity.User;
import org.springframework.lang.Nullable;

@Getter
@NoArgsConstructor
public class MypageResponseDto {

    private String userName;

    @Nullable
    private String[] categoryPreference;

    @Nullable
    private Object[] bookmarkedPopups;

    @Builder
    private MypageResponseDto(String userName, String[] categoryPreference, Object[] bookmarkedPopups) {
        this.userName = userName;
        this.categoryPreference = categoryPreference;
        this.bookmarkedPopups = bookmarkedPopups;
    }

    public static MypageResponseDto from(User user, String[] categoryPreference, Object[] bookmarkedPopups){
        return MypageResponseDto.builder()
                .userName(user.getUsername())
                .categoryPreference(categoryPreference)
                .bookmarkedPopups(bookmarkedPopups)
                .build();
    }
}
