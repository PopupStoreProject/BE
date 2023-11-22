package com.project.kpaas.popup.service;

import com.project.kpaas.classification.entity.Category;
import com.project.kpaas.classification.entity.Hashtag;
import com.project.kpaas.classification.repository.CategoryRepository;
import com.project.kpaas.classification.repository.HashtagRepository;
import com.project.kpaas.global.dto.SuccessResponseDto;
import com.project.kpaas.global.exception.CustomException;
import com.project.kpaas.global.exception.ErrorCode;
import com.project.kpaas.global.security.UserDetailsImpl;
import com.project.kpaas.popup.dto.MessageResponseDto;
import com.project.kpaas.popup.dto.PopupRequestDto;
import com.project.kpaas.popup.dto.PopupResponseDto;
import com.project.kpaas.popup.entity.Popupstore;
import com.project.kpaas.popup.entity.Region;
import com.project.kpaas.popup.repository.PopupRepository;
import com.project.kpaas.popup.repository.RegionRepository;
import com.project.kpaas.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PopupService {

    private final PopupRepository popupRepository;
    private final RegionRepository regionRepository;
    private final CategoryRepository categoryRepository;
    private final HashtagRepository hashtagRepository;


    @Transactional
    public ResponseEntity<MessageResponseDto> addPopup(PopupRequestDto popupRequestDto, UserDetailsImpl userDetails) {

        if (userDetails.getUser().getRole() == UserRole.USER) {
            throw new CustomException(ErrorCode.AUTHORIZATION);
        }

        Optional<Category> foundCategoryName = categoryRepository.findByCategoryName(popupRequestDto.getCategory());
        if (foundCategoryName.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CATEGORY);
        }

        Region requestedRegion = Region.from(popupRequestDto);
        Region region = findRegion(popupRequestDto, requestedRegion);

        Popupstore newPopupStore = Popupstore.of(popupRequestDto, foundCategoryName.get(), userDetails.getUser(), region);
        Set<String> hashtags = new HashSet<>(Arrays.asList(popupRequestDto.getHashtags()));

        for (String h : hashtags) {
            Hashtag hashtag = Hashtag.of(h);
            hashtag.addToPopupStore(newPopupStore);
        }

        popupRepository.save(newPopupStore);
        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "팝업스토어 등록 완료", newPopupStore.getId()));
    }

    // 메인페이지 카테고리 조회
    @Transactional
    public ResponseEntity<List<PopupResponseDto>> searchByCategory(String category) {
        Optional<Category> foundCategory = categoryRepository.findByCategoryName(category);
        if (foundCategory.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CATEGORY);
        }

        List<Popupstore> popupStores = popupRepository.findAllByCategory(foundCategory.get());
        List<PopupResponseDto> popupResponseDto = new ArrayList<>();

        for (Popupstore popupStore : popupStores) {
            List<Hashtag> foundHashtags = hashtagRepository.findAllByPopupstoreId(popupStore.getId());
            String[] hashtags = getHashtags(foundHashtags);
            popupResponseDto.add(PopupResponseDto.of(popupStore, popupStore.getCategory().getCategoryName(), hashtags));
        }
        return ResponseEntity.ok().body(popupResponseDto);
    }

    // 메인페이지 전체 조회
    @Transactional
    public ResponseEntity<List<PopupResponseDto>> getAllPopups() {
        List<Popupstore> popupStores = popupRepository.findAll();
        List<PopupResponseDto> popupResponseDto = new ArrayList<>();

        for (Popupstore popupStore : popupStores) {
            List<Hashtag> foundHashtags = hashtagRepository.findAllByPopupstoreId(popupStore.getId());
            String[] hashtags = getHashtags(foundHashtags);
            popupResponseDto.add(PopupResponseDto.of(popupStore, popupStore.getCategory().getCategoryName(), hashtags));
        }
        return ResponseEntity.ok().body(popupResponseDto);
    }

    // 상세페이지 조회
    @Transactional
    public ResponseEntity<PopupResponseDto> getPopup(Long id) {
        Optional<Popupstore> popupStore = popupRepository.findById(id);
        if (popupStore.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_POPUP);
        }

        List<Hashtag> foundHashtags = hashtagRepository.findAllByPopupstoreId(popupStore.get().getId());
        String[] hashtags = getHashtags(foundHashtags);
        return ResponseEntity.ok().body(PopupResponseDto.of(popupStore.get(), popupStore.get().getCategory().getCategoryName(), hashtags));
    }

    @Transactional
    public ResponseEntity<SuccessResponseDto> updatePopup(Long id, PopupRequestDto popupRequestDto, UserDetailsImpl userDetails) {

        if (userDetails.getUser().getRole() == UserRole.USER) {
            throw new CustomException(ErrorCode.AUTHORIZATION);
        }

        Optional<Popupstore> foundPopupstore = popupRepository.findById(id);
        if (foundPopupstore.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_POPUP);
        }

        List<Hashtag> currentHashtags = hashtagRepository.findAllByPopupstoreId(foundPopupstore.get().getId());
        List<String> inputHastags = new ArrayList<>(Arrays.asList(popupRequestDto.getHashtags()));
        List<String> newHashtags = new ArrayList<>();

        for (Hashtag hashtag : currentHashtags) {
            if(!inputHastags.contains(hashtag.getContent())){
                hashtagRepository.delete(hashtag);
            }
            newHashtags.add(hashtag.getContent());
        }

        for (String input : inputHastags) {
            if(!newHashtags.contains(input)){
                Hashtag hashtag = Hashtag.of(input);
                hashtag.addToPopupStore(foundPopupstore.get());
                hashtagRepository.save(hashtag);
            }
        }

        Optional<Category> category = categoryRepository.findByCategoryName(popupRequestDto.getCategory());
        if (category.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CATEGORY);
        }

        Optional<Region> region = regionRepository.findById(foundPopupstore.get().getRegion().getId());
        if (region.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_REGION);
        }
        region.get().update(popupRequestDto);

        foundPopupstore.get().update(popupRequestDto, category.get(), region.get());

        return ResponseEntity.ok().body(SuccessResponseDto.of("수정이 완료되었습니다.", HttpStatus.OK));
    }

    private static String[] getHashtags(List<Hashtag> foundHashtags) {
        if (foundHashtags.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_HASHTAG);
        }
        String[] hashtags = foundHashtags.stream().map(Hashtag::getContent).toArray(String[]::new);
        return hashtags;
    }

    private Region findRegion(PopupRequestDto popupRequestDto, Region requestedRegion) {
        return regionRepository.findAll().stream()
                .filter(region -> region.getRegionName().equals(popupRequestDto.getRegionName()))
                .findFirst()
                .map(existingRegion -> {
                    if (regionRepository.findById(existingRegion.getId()).isEmpty()) {
                        throw new CustomException(ErrorCode.NOT_FOUND_REGION);
                    }
                    return existingRegion;
                })
                .orElseGet(() -> regionRepository.save(requestedRegion));
    }

}
