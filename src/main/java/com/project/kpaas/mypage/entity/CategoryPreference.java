package com.project.kpaas.mypage.entity;

import com.project.kpaas.classification.entity.Category;
import com.project.kpaas.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)

public class CategoryPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Builder
    private CategoryPreference(User user, Category category) {
        this.user = user;
        this.category = category;
    }

    public static CategoryPreference of(User user, Category category){
        return CategoryPreference.builder()
                .user(user)
                .category(category)
                .build();
    }
}
