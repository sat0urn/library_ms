package com.example.library_ms_project.interceptors;

import nl.bitwalker.useragentutils.UserAgent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class MobileDetectionIntercept implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userAgentHeader = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentHeader);

        if (userAgent.getOperatingSystem().isMobileDevice()) {
            response.setContentType("text/html");

            ClassPathResource templateResource = new ClassPathResource("templates/mobile.html");
            String htmlContent = new String(templateResource.getInputStream().readAllBytes());

            PrintWriter writer = response.getWriter();

            writer.println(htmlContent);
            return false;
        }
        return true;
    }
}
