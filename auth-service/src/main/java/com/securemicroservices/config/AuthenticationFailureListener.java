package com.securemicroservices.config;

import com.securemicroservices.service.impl.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener implements
        ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final HttpServletRequest request;

    private final AuthenticationService authenticationService;

    @Override
    public void onApplicationEvent(@NonNull AuthenticationFailureBadCredentialsEvent e) {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteHost();
        } else {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        authenticationService.loginFailed(ipAddress);
    }
}
