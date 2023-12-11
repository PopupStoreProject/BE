package com.project.kpaas.brandPage.entity;

import com.project.kpaas.brandPage.dto.BrandRequestDto;
import com.project.kpaas.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brandName;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(nullable = true)
    private String brandImageUrl;

    @Column(nullable = true)
    private String instagramUrl;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;


    @Builder
    private Brand(BrandRequestDto brandRequestDto, User user) {
        this.brandName = brandRequestDto.getBrandName();
        this.content = brandRequestDto.getContent();
        this.brandImageUrl = brandRequestDto.getBrandImageUrl();
        this.instagramUrl = brandRequestDto.getInstagramUrl();
        this.user = user;
    }

    public static Brand of(BrandRequestDto brandRequestDto, User user) {
        return Brand.builder()
                .brandRequestDto(brandRequestDto)
                .user(user)
                .build();
    }

    public void update(BrandRequestDto brandRequestDto) {
        this.brandName = brandRequestDto.getBrandName();
        this.brandImageUrl = brandRequestDto.getBrandImageUrl();
        this.content = brandRequestDto.getContent();
        this.instagramUrl = brandRequestDto.getInstagramUrl();
    }
}
