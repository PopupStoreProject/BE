package com.project.kpaas.brandPage.dto;

import com.project.kpaas.brandPage.entity.Brand;
import com.project.kpaas.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@NoArgsConstructor
public class BrandResponseDto {
    private String brandName;
    private String content;
    private String brandImageUrl;

    @Nullable
    private String instagramUrl;

    private String userName;

    @Nullable
    private Object[] registeredPopups;

    @Builder
    private BrandResponseDto(String brandName, String userName, String content, String brandImageUrl, String instagramUrl, Object[] registeredPopups) {
        this.brandName = brandName;
        this.userName = userName;
        this.content = content;
        this.brandImageUrl = brandImageUrl;
        this.instagramUrl = instagramUrl;
        this.registeredPopups = registeredPopups;
    }

    public static BrandResponseDto of(Brand brand, User user, Object[] registeredPopups){
        return BrandResponseDto.builder()
                .brandName(brand.getBrandName())
                .userName(user.getUsername())
                .content(brand.getContent())
                .brandImageUrl(brand.getBrandImageUrl())
                .instagramUrl(brand.getInstagramUrl())
                .registeredPopups(registeredPopups)
                .build();
    }
}
