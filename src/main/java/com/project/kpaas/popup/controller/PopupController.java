package com.project.kpaas.popup.controller;

import com.project.kpaas.global.dto.MessageResponseDto;
import com.project.kpaas.global.security.UserDetailsImpl;
import com.project.kpaas.popup.dto.PopupMsgResponseDto;
import com.project.kpaas.popup.dto.PopupRequestDto;
import com.project.kpaas.popup.dto.PopupResponseDto;
import com.project.kpaas.popup.service.PopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PopupController {

    private final PopupService popupService;

    @PostMapping("/popup")
    public ResponseEntity<PopupMsgResponseDto> popupAdd(@RequestBody PopupRequestDto popupRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return popupService.addPopup(popupRequestDto, userDetails);
    }

    @GetMapping("/popups")
    public ResponseEntity<List<PopupResponseDto>> allPopupGet(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return popupService.getAllPopups(userDetails);
    }

    @GetMapping("/category")
    public ResponseEntity<List<PopupResponseDto>> categorySearch(@RequestParam(value = "c") String category, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return popupService.searchByCategory(category, userDetails);
    }

    @GetMapping("/popup/{id}")
    public ResponseEntity<PopupResponseDto> popupGet(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return popupService.getPopup(id, userDetails);
    }

    @PutMapping("/popup/{id}")
    public ResponseEntity<MessageResponseDto> popupUpdate(@PathVariable Long id, @RequestBody PopupRequestDto popupRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return popupService.updatePopup(id, popupRequestDto, userDetails);
    }

    @DeleteMapping("/popup/{id}")
    public ResponseEntity<MessageResponseDto> popupDelete(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return popupService.deletePopup(id, userDetails.getUser());
    }

    // radius는 meter단위(ex. 1000 = 1000m = 1km)
    @GetMapping("/gps")
    public ResponseEntity<List<PopupResponseDto>> gpsSearch(@RequestParam(value = "lat") double lat,
                                                            @RequestParam(value = "lon") double lon,
                                                            @RequestParam(value = "radius") double radius,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return popupService.searchByRadius(lat, lon, radius, userDetails);
    }

}
