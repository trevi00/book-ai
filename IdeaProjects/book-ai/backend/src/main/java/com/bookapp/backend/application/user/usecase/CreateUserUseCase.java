package com.bookapp.backend.application.user.usecase;

import com.bookapp.backend.application.user.dto.UserCreateRequest;
import com.bookapp.backend.application.user.dto.UserResponse;
import com.bookapp.backend.domain.user.User;
import com.bookapp.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CreateUserUseCase {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserResponse execute(UserCreateRequest request) {
        try {
            log.info("사용자 등록 시작");
            
            validateEmailNotExists(request.getEmail());
            log.info("이메일 중복 검증 완료");
            
            // 도메인 검증을 위해 먼저 User 객체 생성 (암호화 전 비밀번호로)
            User user = User.builder()
                    .email(request.getEmail())
                    .password(request.getPassword()) // 원본 비밀번호로 도메인 검증
                    .nickname(request.getNickname())
                    .build();
            log.info("User 도메인 객체 생성 및 검증 완료");
            
            // 검증 통과 후 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            user.setEncodedPassword(encodedPassword);
            log.info("비밀번호 암호화 완료");
            
            User savedUser = userRepository.save(user);
            log.info("사용자 저장 완료 - ID: {}", savedUser.getId());
            
            UserResponse response = UserResponse.from(savedUser);
            log.info("사용자 등록 성공");
            return response;
            
        } catch (Exception e) {
            log.error("사용자 등록 중 오류 발생", e);
            throw e;
        }
    }
    
    private void validateEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");
        }
    }
}