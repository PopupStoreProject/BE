package com.project.kpaas.mypage.entity;

import com.project.kpaas.popup.entity.Popupstore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.project.kpaas.user.entity.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popupstore_id", nullable = false)
    private Popupstore popupstore;

    @Builder
    private Bookmark(User user, Popupstore popupstore) {
        this.user = user;
        this.popupstore = popupstore;
    }

    public static Bookmark of(Popupstore popupstore, User user){
        return Bookmark.builder()
                .popupstore(popupstore)
                .user(user)
                .build();
    }
}
