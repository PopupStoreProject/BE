package com.project.kpaas.popup.entity;

import com.project.kpaas.mypage.entity.Bookmark;
import com.project.kpaas.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private Date postdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popstore_id")
    private Popupstore popupstore;

    @Builder
    private BlogReview(String title, String link, Date postdate, Popupstore popupstore) {
        this.title = title;
        this.link = link;
        this.postdate = postdate;
        this.popupstore = popupstore;
    }

    public static BlogReview of(BlogReview blogReview){
        return BlogReview.builder()
                .title(blogReview.title)
                .link(blogReview.getLink())
                .postdate(blogReview.getPostdate())
                .build();
    }
}
