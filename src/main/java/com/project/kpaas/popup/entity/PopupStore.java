package com.project.kpaas.popup.entity;

import com.project.kpaas.popup.dto.PopupRequestDto;
import lombok.Builder;
import lombok.Getter;
import com.project.kpaas.user.entity.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class PopupStore {
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
    private String gps;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Builder
    private PopupStore(PopupRequestDto popupRequestDto, User user) {
        this.popupName = popupRequestDto.getPopupName();
        this.content = popupRequestDto.getContent();
        this.startDate = popupRequestDto.getStartDate();
        this.endDate = popupRequestDto.getEndDate();
        this.gps = popupRequestDto.getGps();
        this.user = user;
    }

    public static PopupStore from(PopupRequestDto popupRequestDto, User user) {
        return PopupStore.builder()
                .popupRequestDto(popupRequestDto)
                .user(user)
                .build();
    }
}
