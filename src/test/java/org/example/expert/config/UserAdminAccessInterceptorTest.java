package org.example.expert.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.expert.domain.common.exception.AdminAuthenticationFailedException;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class UserAdminAccessInterceptorTest {

    @InjectMocks
    private UserAdminAccessInterceptor userAdminAccessInterceptor;

    @Test
    void 인증되지_않은_사용자의_접근은_차단된다() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Mockito.when(request.getAttribute("userRole")).thenReturn(String.valueOf(UserRole.USER));
        Mockito.when(request.getAttribute("userId")).thenReturn(1L);

        assertThrows(AdminAuthenticationFailedException.class,
                () -> userAdminAccessInterceptor.preHandle(request, response, null));
    }

    @Test
    void 어드민_사용자는_접근을_허용한다() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        UserRoleChangeRequest userRoleChangeRequest = new UserRoleChangeRequest(String.valueOf(UserRole.USER));
        ObjectMapper objectMapper = new ObjectMapper();

        request.setAttribute("userRole", String.valueOf(UserRole.ADMIN));
        request.setAttribute("userId", 1L);
        request.setRequestURI("/admin/users/1");
        request.setContent(objectMapper.writeValueAsString(userRoleChangeRequest).getBytes());

        boolean result = userAdminAccessInterceptor.preHandle(request, response, null);

        assertTrue(result);
    }
}
