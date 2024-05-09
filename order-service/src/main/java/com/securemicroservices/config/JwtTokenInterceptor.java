package com.securemicroservices.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenInterceptor implements ClientHttpRequestInterceptor {

    private final JwtTokenExtractor jwtTokenExtractor;

    @Value("${secret.shared}")
    private String sharedSecret;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String jwtToken = jwtTokenExtractor.extractJwtToken();
        if (jwtToken != null) {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, jwtToken);
        }

        request.getHeaders().add("X-SHARED-SECRET", sharedSecret);

        return execution.execute(request, body);
    }
}