package com.project.kpaas.popup.entity;

import com.project.kpaas.classification.entity.Category;
import com.project.kpaas.classification.entity.Hashtag;
import com.project.kpaas.popup.dto.PopupRequestDto;
import lombok.Builder;
import lombok.Getter;
import com.project.kpaas.user.entity.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Popupstore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String popupName;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @Column(nullable = false)
    private String openingHours;

    @Column(nullable = false)
    private String gps;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String homepageUrl;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "popupstore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hashtag> hashtags = new ArrayList<>();


    @Builder
    private Popupstore(PopupRequestDto popupRequestDto, Category category, User user, Region region) {
        this.popupName = popupRequestDto.getPopupName();
        this.content = popupRequestDto.getContent();
        this.category = category;
        this.startDate = popupRequestDto.getStartDate();
        this.endDate = popupRequestDto.getEndDate();
        this.openingHours = popupRequestDto.getOpeningHours();
        this.gps = popupRequestDto.getGps();
        this.imageUrl = popupRequestDto.getImageUrl();
        this.homepageUrl = popupRequestDto.getHomepageUrl();
        this.user = user;
        this.region = region;
    }

    public static Popupstore of(PopupRequestDto popupRequestDto, Category category, User user, Region region) {
        return Popupstore.builder()
                .popupRequestDto(popupRequestDto)
                .category(category)
                .user(user)
                .region(region)
                .build();
    }
}