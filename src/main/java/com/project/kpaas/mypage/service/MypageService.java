package com.project.kpaas.mypage.service;

import com.project.kpaas.classification.entity.Category;
import com.project.kpaas.classification.repository.CategoryRepository;
import com.project.kpaas.mypage.dto.MypageRequestDto;
import com.project.kpaas.mypage.dto.MypageResponseDto;
import com.project.kpaas.global.dto.MessageResponseDto;
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

import static com.project.kpaas.global.exception.ErrorCode.NOT_FOUND_CATEGORY;


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
    public MessageResponseDto updateMyInfo(MypageRequestDto mypageRequestDto, User user) {
        Optional<User> foundUser = userRepository.findById(user.getId());
        if(foundUser.isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
        foundUser.get().update(mypageRequestDto);

        List<String> newCategoryPreference = mypageRequestDto.getCategoryPreference();
        List<CategoryPreference> foundCategoryPreferences = categoryPreferenceRepository.findAllByUserId(foundUser.get().getId());

        for (CategoryPreference existingPreference : foundCategoryPreferences) {
            String existingCategory = existingPreference.getCategory().getCategoryName();
            if (!newCategoryPreference.contains(existingCategory)) {
                categoryPreferenceRepository.delete(existingPreference);
            }
        }

        for (String newCategory : newCategoryPreference) {
            if (foundCategoryPreferences.stream().noneMatch(c -> c.getCategory().getCategoryName().equals(newCategory))) {
                Optional<Category> foundCategory = categoryRepository.findByCategoryName(newCategory);
                if (foundCategory.isEmpty()) {
                    throw new CustomException(NOT_FOUND_CATEGORY);
                }
                categoryPreferenceRepository.save(CategoryPreference.of(user, foundCategory.get()));
            }
        }

        return MessageResponseDto.of("수정이 완료 되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<MessageResponseDto> pushLike(Long id, User user) {

        Optional<Popupstore> foundPopupstore = popupRepository.findById(id);
        if (foundPopupstore.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_POPUP);
        }

        Optional<User> foundMember = userRepository.findById(user.getId());

        Optional<Bookmark> bookmark = bookmarkRepository.findByPopupstoreIdAndUserId(id, foundMember.get().getId());
        if (bookmark.isEmpty()) {
            Bookmark newBookmark = Bookmark.of(foundPopupstore.get(), foundMember.get());
            bookmarkRepository.save(newBookmark);
            return ResponseEntity.ok().body(MessageResponseDto.of("즐겨찾기 추가", HttpStatus.OK));
        } else {
            bookmarkRepository.deleteByPopupstoreId(foundPopupstore.get().getId());
            return ResponseEntity.ok().body(MessageResponseDto.of("즐겨찾기 취소", HttpStatus.OK));
        }
    }


}
