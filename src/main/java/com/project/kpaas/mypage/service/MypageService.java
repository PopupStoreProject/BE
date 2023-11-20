package com.project.kpaas.mypage.service;

import com.project.kpaas.classification.entity.Category;
import com.project.kpaas.classification.repository.CategoryRepository;
import com.project.kpaas.mypage.dto.MypageResponseDto;
import com.project.kpaas.global.dto.SuccessResponseDto;
import com.project.kpaas.global.exception.CustomException;
import com.project.kpaas.global.exception.ErrorCode;
import com.project.kpaas.mypage.entity.Bookmark;
import com.project.kpaas.mypage.entity.CategoryPreference;
import com.project.kpaas.mypage.repository.BookmarkRepository;
import com.project.kpaas.mypage.repository.CategoryPreferenceRepository;
import com.project.kpaas.popup.entity.Popupstore;
import com.project.kpaas.popup.repository.PopupRepository;
import com.project.kpaas.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.project.kpaas.user.entity.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class MypageService {

    private final PopupRepository popupRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryPreferenceRepository categoryPreferenceRepository;

    @Transactional
    public MypageResponseDto getMyInfo(User user) {

        Optional<User> foundUser = userRepository.findById(user.getId());
        if(foundUser.isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        List<String> foundCategories = categoryPreferenceRepository.findAllByUserId(user.getId())
                .stream()
                .map(categoryPreference -> categoryRepository.findById(categoryPreference.getCategory().getId()))
                .filter(Optional::isPresent)
                .map(foundCategory -> foundCategory.get().getCategoryName())
                .collect(Collectors.toList());

        List<List<String>> bookmarkedPopups = bookmarkRepository.findAllByUserId(user.getId())
                .stream()
                .map(bookmark -> {
                    Popupstore popupstore = bookmark.getPopupstore();
                    return List.of(popupstore.getPopupName(), popupstore.getCategory().getCategoryName(), popupstore.getImageUrl());
                })
                .collect(Collectors.toList());

        return MypageResponseDto.from(foundUser.get(), foundCategories.toArray(String[]::new), bookmarkedPopups.toArray(Object[]::new));
    }

    @Transactional
    public ResponseEntity<SuccessResponseDto> pushLike(Long id, User user) {

        Optional<Popupstore> foundPopupstore = popupRepository.findById(id);
        if (foundPopupstore.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_POPUP);
        }

        Optional<User> foundMember = userRepository.findById(user.getId());

        Optional<Bookmark> bookmark = bookmarkRepository.findByPopupstoreIdAndUserId(id, foundMember.get().getId());
        if (bookmark.isEmpty()) {
            Bookmark newBookmark = Bookmark.of(foundPopupstore.get(), foundMember.get());
            bookmarkRepository.save(newBookmark);
            return ResponseEntity.ok().body(SuccessResponseDto.of("즐겨찾기 추가", HttpStatus.OK));
        } else {
            bookmarkRepository.deleteByPopupstoreId(foundPopupstore.get().getId());
            return ResponseEntity.ok().body(SuccessResponseDto.of("즐겨찾기 취소", HttpStatus.OK));
        }
    }


}
