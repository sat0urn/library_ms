package com.example.library_ms_project.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // Get the user's roles
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                // Redirect to the admin page if the user has the ADMIN role
                setDefaultTargetUrl("/admin");
                break;
            } else {
                // Redirect to the user profile page if the user has the USER role
                setDefaultTargetUrl("/user/profile");
            }
        }
        try {
            super.onAuthenticationSuccess(request, response, authentication);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
