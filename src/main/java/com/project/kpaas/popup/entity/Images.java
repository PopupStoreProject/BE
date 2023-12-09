package com.project.kpaas.popup.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "MEDIUMBLOB")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popupstore_id",nullable = false)
    private Popupstore popupstore;

    @Builder
    private Images(String imageUrl, Popupstore popupstore) {
        this.imageUrl = imageUrl;
        this.popupstore = popupstore;
    }

    public static Images of(String imageUrl) {
        return Images.builder()
                .imageUrl(imageUrl)
                .build();
    }

    public void addToPopupStore(Popupstore popupstore){
        this.popupstore = popupstore;
        popupstore.getImages().add(this);
    }
}
