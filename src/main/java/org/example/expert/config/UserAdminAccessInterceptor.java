package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.exception.AdminAuthenticationFailedException;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class UserAdminAccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        Long userId = (Long) request.getAttribute("userId");
        String requestTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        UserRole userRole = UserRole.of((String) request.getAttribute("userRole"));

        if (!UserRole.ADMIN.equals(userRole)) {
            log.warn("loginId : {}", userId);
            log.warn("time : {}", requestTime);
            throw new AdminAuthenticationFailedException("관리자 권한이 없습니다.");
        }

        log.info("loginId : {}", userId);
        log.info("time : {}", requestTime);
        log.info("requestURL : {}", request.getRequestURI());

        return true;
    }
}
