package com.project.kpaas.user.service;

import com.project.kpaas.classification.entity.Category;
import com.project.kpaas.classification.repository.CategoryRepository;
import com.project.kpaas.global.dto.MessageResponseDto;
import com.project.kpaas.global.exception.CustomException;
import com.project.kpaas.global.util.JwtUtil;
import com.project.kpaas.mypage.entity.CategoryPreference;
import com.project.kpaas.mypage.repository.CategoryPreferenceRepository;
import com.project.kpaas.user.dto.LoginRequestDto;
import com.project.kpaas.user.dto.SignupRequestDto;
import com.project.kpaas.user.repository.UserRepository;
import com.project.kpaas.user.entity.User;
import com.project.kpaas.user.entity.UserRole;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.project.kpaas.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryPreferenceRepository categoryPreferenceRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    public ResponseEntity<MessageResponseDto> signup(SignupRequestDto signupRequestDto) {

        String username = signupRequestDto.getUsername();
        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        Optional<User> foundUser = userRepository.findByEmail(email);
        if (foundUser.isPresent()) {
            throw new CustomException(DUPLICATE_USER);
        }

        UserRole role = UserRole.USER;

        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new CustomException(INVALID_ADMIN_TOKEN);
            }
            role = UserRole.ADMIN;
        }

        User user = User.of(username, email, password, role);
        userRepository.save(user);

        List<String> categoryPreference = signupRequestDto.getCategoryPreference();
        for (String category : categoryPreference) {
            Optional<Category> foundCategory = categoryRepository.findByCategoryName(category);
            if (foundCategory.isEmpty()) {
                throw new CustomException(NOT_FOUND_CATEGORY);
            }
            categoryPreferenceRepository.save(CategoryPreference.of(user, foundCategory.get()));
        }

        return ResponseEntity.ok()
                .body(MessageResponseDto.of("회원가입 완료", HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<MessageResponseDto> login(LoginRequestDto loginRequestDto) {

        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException(NOT_FOUND_USER);
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new CustomException(NOT_MATCH_PASSWORD);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername(), user.get().getRole()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(MessageResponseDto.of("로그인 성공", HttpStatus.OK));
    }

}
