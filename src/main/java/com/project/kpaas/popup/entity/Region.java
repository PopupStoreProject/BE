package com.project.kpaas.popup.entity;

import com.project.kpaas.popup.dto.PopupRequestDto;
import com.project.kpaas.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String regionName;

    @Builder
    private Region(PopupRequestDto popupRequestDto) {
        this.regionName = popupRequestDto.getRegionName();
    }

    public static Region from(PopupRequestDto popupRequestDto) {
        return Region.builder()
                .popupRequestDto(popupRequestDto)
                .build();
    }

}
