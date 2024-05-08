package com.securemicroservices.config;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@AllArgsConstructor
public class JwtTokenInterceptor implements ClientHttpRequestInterceptor {

    private JwtTokenExtractor jwtTokenExtractor;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String jwtToken = jwtTokenExtractor.extractJwtToken();
        if (jwtToken != null) {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, jwtToken);
        }
        return execution.execute(request, body);
    }
}