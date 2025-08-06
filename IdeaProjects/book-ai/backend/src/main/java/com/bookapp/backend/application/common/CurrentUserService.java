package com.bookapp.backend.application.common;

import com.bookapp.backend.web.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    
    /**
     * 현재 인증된 사용자의 ID를 반환합니다.
     * SecurityContext에서 UserPrincipal을 통해 사용자 ID를 가져옵니다.
     */
    public Long getCurrentUserId() {
        UserPrincipal userPrincipal = getCurrentUserPrincipal();
        if (userPrincipal == null) {
            throw new IllegalStateException("인증되지 않은 사용자입니다");
        }
        
        return userPrincipal.getUserId();
    }
    
    /**
     * 현재 인증된 사용자의 이메일을 반환합니다.
     */
    public String getCurrentUserEmail() {
        UserPrincipal userPrincipal = getCurrentUserPrincipal();
        if (userPrincipal == null) {
            throw new IllegalStateException("인증되지 않은 사용자입니다");
        }
        
        return userPrincipal.getEmail();
    }
    
    /**
     * SecurityContext에서 UserPrincipal을 추출합니다.
     */
    private UserPrincipal getCurrentUserPrincipal() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal) {
            return (UserPrincipal) principal;
        }
        
        return null;
    }
}