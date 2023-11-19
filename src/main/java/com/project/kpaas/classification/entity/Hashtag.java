package com.project.kpaas.classification.entity;

import com.project.kpaas.popup.entity.Popupstore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popupstore_id",nullable = false)
    private Popupstore popupstore;


    @Builder
    private Hashtag(String content, Popupstore popupstore) {
        this.content = content;
        this.popupstore = popupstore;
    }

    public static Hashtag of(String content) {
        return Hashtag.builder()
                .content(content)
                .build();
    }

    public void addToPopupStore(Popupstore popupstore){
        this.popupstore = popupstore;
        popupstore.getHashtags().add(this);
    }
}
