package com.bookapp.backend.application.user;

import com.bookapp.backend.application.user.dto.LoginRequest;
import com.bookapp.backend.application.user.dto.UserCreateRequest;
import com.bookapp.backend.application.user.dto.UserResponse;
import com.bookapp.backend.application.user.usecase.CreateUserUseCase;
import com.bookapp.backend.application.user.usecase.LoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserApplicationService {
    
    private final CreateUserUseCase createUserUseCase;
    private final LoginUseCase loginUseCase;
    
    public UserResponse createUser(UserCreateRequest request) {
        return createUserUseCase.execute(request);
    }
    
    public UserResponse login(LoginRequest request) {
        return loginUseCase.execute(request);
    }
}