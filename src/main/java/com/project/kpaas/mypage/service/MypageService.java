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
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
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
                    return List.of(popupstore.getPopupName(), popupstore.getCategory().getCategoryName());
                })
                .collect(Collectors.toList());

        return MypageResponseDto.from(foundUser.get(), foundCategories.toArray(String[]::new), bookmarkedPopups.toArray(Object[]::new));
    }

    @Transactional
    public MessageResponseDto updateMyInfo(@RequestBody  MypageRequestDto mypageRequestDto, User user) {
        Optional<User> foundUser = userRepository.findById(user.getId());
        if(foundUser.isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
        foundUser.get().update(mypageRequestDto);

        List<String> newCategoryPreference = new ArrayList<>(Arrays.asList(mypageRequestDto.getCategoryPreference()));
        List<CategoryPreference> foundCategoryPreferences = categoryPreferenceRepository.findAllByUserId(foundUser.get().getId());
        if (newCategoryPreference.isEmpty()) {
            categoryPreferenceRepository.deleteAllByUserId(foundUser.get().getId());
        }
        changeCategoryPreference(user, newCategoryPreference, foundCategoryPreferences);

        return MessageResponseDto.of("수정이 완료 되었습니다.", HttpStatus.OK);
    }


    private void changeCategoryPreference(User user, List<String> newCategoryPreference, List<CategoryPreference> foundCategoryPreferences) {
        foundCategoryPreferences.stream()
                .filter(existingPreference -> !newCategoryPreference.contains(existingPreference.getCategory().getCategoryName()))
                .forEach(categoryPreferenceRepository::delete);

        newCategoryPreference.stream()
                .filter(newCategory -> foundCategoryPreferences.stream().noneMatch(c -> c.getCategory().getCategoryName().equals(newCategory)))
                .map(newCategory -> categoryRepository.findByCategoryName(newCategory)
                        .orElseThrow(() -> new CustomException(NOT_FOUND_CATEGORY)))
                .forEach(foundCategory -> categoryPreferenceRepository.save(CategoryPreference.of(user, foundCategory)));
    }

    @Transactional
    public ResponseEntity<MessageResponseDto> pushLike(Long id, User user) {

        Optional<Popupstore> foundPopupstore = popupRepository.findById(id);
        if (foundPopupstore.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_POPUP);
        }

        Optional<User> foundMember = userRepository.findById(user.getId());

        Optional<Bookmark> bookmark = bookmarkRepository.findByPopupstoreIdAndUserId(id, foundMember.get().getId());
        return getBookMark(foundPopupstore, foundMember, bookmark);
    }

    private ResponseEntity<MessageResponseDto> getBookMark(Optional<Popupstore> foundPopupstore, Optional<User> foundMember, Optional<Bookmark> bookmark) {
        if (bookmark.isEmpty()) {
            Bookmark newBookmark = Bookmark.of(foundPopupstore.get(), foundMember.get());
            bookmarkRepository.save(newBookmark);
            return ResponseEntity.ok().body(MessageResponseDto.of("즐겨찾기 추가", HttpStatus.OK));
        }
        bookmarkRepository.deleteByPopupstoreId(foundPopupstore.get().getId());
        return ResponseEntity.ok().body(MessageResponseDto.of("즐겨찾기 취소", HttpStatus.OK));
    }


}
