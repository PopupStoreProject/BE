package com.project.kpaas.user.entity;

import com.project.kpaas.mypage.dto.MypageRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Column(nullable = true, columnDefinition = "MEDIUMBLOB")
    private String userImage;

    @Builder
    private User(String username, String email, String password, UserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User of(String username, String email, String password, UserRole role) {
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .build();
    }

    public void update(MypageRequestDto mypageRequestDto) {
        this.username = mypageRequestDto.getUsername();
        this.userImage = mypageRequestDto.getUserImage();
    }

}
