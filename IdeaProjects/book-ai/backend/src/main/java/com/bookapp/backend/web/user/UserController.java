package com.bookapp.backend.web.user;

import com.bookapp.backend.application.user.UserApplicationService;
import com.bookapp.backend.application.user.dto.LoginRequest;
import com.bookapp.backend.application.user.dto.UserCreateRequest;
import com.bookapp.backend.application.user.dto.UserResponse;
import com.bookapp.backend.web.common.ApiResponse;
import com.bookapp.backend.web.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserApplicationService userApplicationService;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserApplicationService userApplicationService, JwtUtils jwtUtils) {
        this.userApplicationService = userApplicationService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserCreateRequest request) {
        try {
            System.out.println("DEBUG: Register request received - " + request.getEmail());
            UserResponse response = userApplicationService.createUser(request);
            System.out.println("DEBUG: User created successfully - " + response.getId());
            return ApiResponse.success(response, "회원가입이 완료되었습니다");
        } catch (Exception e) {
            System.out.println("DEBUG: Exception in register - " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        UserResponse userResponse = userApplicationService.login(request);
        
        // JWT 토큰 생성
        String token = jwtUtils.generateJwtToken(userResponse.getEmail(), userResponse.getId());
        
        // 응답 데이터 구성
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("user", userResponse);
        responseData.put("token", token);
        responseData.put("tokenType", "Bearer");
        
        return ApiResponse.success(responseData, "로그인이 완료되었습니다");
    }
}