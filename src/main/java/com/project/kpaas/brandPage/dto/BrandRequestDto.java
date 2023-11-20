package com.project.kpaas.brandPage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@NoArgsConstructor
public class BrandRequestDto {
    private String brandName;
    private String content;
    private String brandImageUrl;

    @Nullable
    private String instagramUrl;

}
