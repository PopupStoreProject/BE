package com.project.kpaas.popup.service;

import com.project.kpaas.brandPage.entity.Brand;
import com.project.kpaas.brandPage.repository.BrandRepository;
import com.project.kpaas.classification.entity.Category;
import com.project.kpaas.classification.entity.Hashtag;
import com.project.kpaas.classification.repository.CategoryRepository;
import com.project.kpaas.classification.repository.HashtagRepository;
import com.project.kpaas.global.dto.MessageResponseDto;
import com.project.kpaas.global.exception.CustomException;
import com.project.kpaas.global.exception.ErrorCode;
import com.project.kpaas.global.security.UserDetailsImpl;
import com.project.kpaas.global.util.ClientUtil;
import com.project.kpaas.global.util.RequestUtilCrawling;
import com.project.kpaas.global.util.RequestUtilVector;
import com.project.kpaas.mypage.entity.Bookmark;
import com.project.kpaas.mypage.repository.BookmarkRepository;
import com.project.kpaas.popup.dto.PopupMsgResponseDto;
import com.project.kpaas.popup.dto.PopupRequestDto;
import com.project.kpaas.popup.dto.PopupResponseDto;
import com.project.kpaas.popup.entity.BlogReview;
import com.project.kpaas.popup.entity.Images;
import com.project.kpaas.popup.entity.Popupstore;
import com.project.kpaas.popup.entity.Region;
import com.project.kpaas.popup.repository.*;
import com.project.kpaas.user.entity.User;
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
    private final PopupRepositoryImpl popupRepositoryImpl;
    private final RegionRepository regionRepository;
    private final CategoryRepository categoryRepository;
    private final HashtagRepository hashtagRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BrandRepository brandRepository;
    private final BlogRepsitory blogRepsitory;
    private final ImageRepository imageRepository;
    private final ClientUtil clientUtil;
    private final RequestUtilCrawling requestUtil;
    private final RequestUtilVector requestUtilVector;


    @Transactional
    public ResponseEntity<PopupMsgResponseDto> addPopup(PopupRequestDto popupRequestDto, UserDetailsImpl userDetails) throws Exception {

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
        String hashtagString = "";

        for (String h : hashtags) {
            Hashtag hashtag = Hashtag.of(h);
            hashtag.addToPopupStore(newPopupStore);
            hashtagString.concat(h);
        }

        Set<String> newImages = new HashSet<>(Arrays.asList(popupRequestDto.getImages()));
        for(String i : newImages) {
            Images images = Images.of(i);
            images.addToPopupStore(newPopupStore);
        }

        popupRepository.saveAndFlush(newPopupStore);
        requestUtil.sendRequest(newPopupStore.getId(), newPopupStore.getPopupName(), newPopupStore.getStartDate());
        requestUtilVector.sendRequest(newPopupStore.getId(), newPopupStore.getPopupName(), newPopupStore.getContent(), newPopupStore.getCategory().getCategoryName(), hashtagString);
        return ResponseEntity.ok().body(PopupMsgResponseDto.of(HttpStatus.OK.value(), "팝업스토어 등록 완료", newPopupStore.getId()));
    }

    // 메인페이지 카테고리 조회
    @Transactional
    public ResponseEntity<List<PopupResponseDto>> searchByCategory(String category, UserDetailsImpl userDetails) {
        Optional<Category> foundCategory = categoryRepository.findByCategoryName(category);
        if (foundCategory.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CATEGORY);
        }

        List<Popupstore> popupStores = popupRepository.findAllByCategory(foundCategory.get());
        List<PopupResponseDto> popupResponseDto = new ArrayList<>();

        for (Popupstore popupStore : popupStores) {
            List<Hashtag> foundHashtags = hashtagRepository.findAllByPopupstoreId(popupStore.getId());
            String[] hashtags = getHashtags(foundHashtags);

            List<Images> foundImages = imageRepository.findAllByPopupstoreId(popupStore.getId());
            String[] images = getImages(foundImages);

            String like = getLike(popupStore.getId(), userDetails);

            Optional<Region> foundRegion = regionRepository.findById(popupStore.getRegion().getId());
            String regionName = foundRegion.get().getRegionName();
            popupResponseDto.add(PopupResponseDto.of(popupStore, popupStore.getCategory().getCategoryName(), hashtags, images, like, regionName));
        }
        return ResponseEntity.ok().body(popupResponseDto);
    }

    // 메인페이지 전체 조회
    @Transactional
    public ResponseEntity<List<PopupResponseDto>> getAllPopups(UserDetailsImpl userDetails) {
        List<Popupstore> popupStores = popupRepository.findAll();
        List<PopupResponseDto> popupResponseDto = new ArrayList<>();

        for (Popupstore popupStore : popupStores) {
            List<Hashtag> foundHashtags = hashtagRepository.findAllByPopupstoreId(popupStore.getId());
            String[] hashtags = getHashtags(foundHashtags);

            List<Images> foundImages = imageRepository.findAllByPopupstoreId(popupStore.getId());
            String[] images = getImages(foundImages);

            String like = getLike(popupStore.getId(), userDetails);

            Optional<Region> foundRegion = regionRepository.findById(popupStore.getRegion().getId());
            String regionName = foundRegion.get().getRegionName();
            popupResponseDto.add(PopupResponseDto.of(popupStore, popupStore.getCategory().getCategoryName(), hashtags, images, like, regionName));
        }
        return ResponseEntity.ok().body(popupResponseDto);
    }

    // 상세페이지 조회
    @Transactional
    public ResponseEntity<PopupResponseDto> getPopup(Long id, UserDetailsImpl userDetails) {
        Optional<Popupstore> popupStore = popupRepository.findById(id);
        if (popupStore.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_POPUP);
        }

        List<BlogReview> foundBlogReviews = blogRepsitory.findAllByPopupstoreId(popupStore.get().getId());
        List<Map<String,String>> blogReiews = new ArrayList<>();
        for (BlogReview b : foundBlogReviews) {
            Map<String,String> blogReviewsDetails = new HashMap<>();
            blogReviewsDetails.put("link", b.getLink());
            blogReviewsDetails.put("title", b.getTitle());
            blogReiews.add(blogReviewsDetails);
        }

        List<Hashtag> foundHashtags = hashtagRepository.findAllByPopupstoreId(popupStore.get().getId());
        String[] hashtags = getHashtags(foundHashtags);

        List<Images> foundImages = imageRepository.findAllByPopupstoreId(popupStore.get().getId());
        String[] images = getImages(foundImages);

        String like = getLike(id, userDetails);

        Optional<Brand> brand = brandRepository.findByUserId(popupStore.get().getUser().getId());
        if(brand.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_BRAND_IMAGE);
        }

        Optional<Region> foundRegion = regionRepository.findById(popupStore.get().getRegion().getId());
        String regionName = foundRegion.get().getRegionName();
        return ResponseEntity.ok().body(PopupResponseDto.of(popupStore.get(), popupStore.get().getCategory().getCategoryName(), hashtags, blogReiews.toArray(), images, like, brand.get().getBrandImageUrl(), regionName));
    }

    @Transactional
    public ResponseEntity<MessageResponseDto> updatePopup(Long id, PopupRequestDto popupRequestDto, UserDetailsImpl userDetails) throws Exception {

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
        String hashtagString = "";

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
            hashtagString.concat(input);
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

        Popupstore p = foundPopupstore.get();
        requestUtilVector.sendRequest(p.getId(), p.getPopupName(), p.getContent(), p.getCategory().getCategoryName(), hashtagString);
        return ResponseEntity.ok().body(MessageResponseDto.of("수정이 완료되었습니다.", HttpStatus.OK));
    }

    // 반경과 현재 위치 가지고, MySQL에서 좌표 가져오는 코드
    public ResponseEntity<List<PopupResponseDto>> searchByRadius(double lat, double lon, double radius, UserDetailsImpl userDetails) {
        List<Popupstore> allPopups = popupRepository.findAll();
        if (allPopups.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_POPUP);
        }

        List<PopupResponseDto> popupResponseDto = new ArrayList<>();
        for (Popupstore p : allPopups) {
            double popupLat = p.getGps().getX();
            double popupLon = p.getGps().getY();
            double distance = distance(popupLat, popupLon, lat, lon);
            if (distance <= radius) {
                List<Hashtag> foundHashtags = hashtagRepository.findAllByPopupstoreId(p.getId());
                String[] hashtags = getHashtags(foundHashtags);

                List<Images> foundImages = imageRepository.findAllByPopupstoreId(p.getId());
                String[] images = getImages(foundImages);

                String like = getLike(p.getId(), userDetails);

                Optional<Region> foundRegion = regionRepository.findById(p.getRegion().getId());
                String regionName = foundRegion.get().getRegionName();
                popupResponseDto.add(PopupResponseDto.of(p, p.getCategory().getCategoryName(), hashtags, images, like, regionName));
            } else {
                throw new CustomException(ErrorCode.NOT_FOUND_NEAR_POPUP);
            }
        }

        return ResponseEntity.ok().body(popupResponseDto);
    }

    @Transactional
    public ResponseEntity<MessageResponseDto> deletePopup(Long id, User user) throws Exception {

        if (user.getRole() == UserRole.USER) {
            throw new CustomException(ErrorCode.AUTHORIZATION);
        }

        Optional<Popupstore> popupstore = popupRepository.findByIdAndUser(id, user);
        if (popupstore.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_POPUP);
        }

        imageRepository.deleteByPopupstoreId(popupstore.get().getId());
        hashtagRepository.deleteByPopupstoreId(popupstore.get().getId());
        bookmarkRepository.deleteByPopupstoreId(popupstore.get().getId());
        popupRepository.deleteById(popupstore.get().getId());

        requestUtilVector.sendRequestDelete(popupstore.get().getId());
        return ResponseEntity.ok().body(MessageResponseDto.of("삭제가 완료되었습니다.", HttpStatus.OK));
    }

    // 두 거리 사이 구해서 반경이 해당 범위 안이면 ok
    private static double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;;

        return dist;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private static String[] getImages(List<Images> foundImages) {
        if (foundImages.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_IMAGE);
        }
        String[] images = foundImages.stream().map(Images::getImageUrl).toArray(String[]::new);
        return images;
    }


    private static String[] getHashtags(List<Hashtag> foundHashtags) {
        String[] hashtags = foundHashtags.stream().map(Hashtag::getContent).toArray(String[]::new);
        return hashtags;
    }

    private String getLike(Long id, UserDetailsImpl userDetails) {
        Optional<Bookmark> userBookmark = bookmarkRepository.findByPopupstoreIdAndUserId(id, userDetails.getUser().getId());
        String like = "N";
        if (userBookmark.isPresent()) {
            like = "Y";
        }
        return like;
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
