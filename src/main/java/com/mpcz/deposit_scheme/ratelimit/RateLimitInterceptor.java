package com.mpcz.deposit_scheme.ratelimit;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mpcz.deposit_scheme.backend.api.services.RateLimitService;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimitService rateLimitService;


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String ip = request.getRemoteAddr();
        String api = request.getRequestURI();


        // 1️⃣ IP based
        if (!rateLimitService
                .resolveBucket("IP:" + ip, 100)
                .tryConsume(1)) {
            return reject(response, "IP rate limit exceeded");
        }


        // 3️⃣ API based
        if (!rateLimitService
                .resolveBucket("API:" + api, 30)
                .tryConsume(1)) {
            return reject(response, "API rate limit exceeded");
        }

        return true;
    }

    private boolean reject(HttpServletResponse response, String msg)
            throws IOException {
        response.setStatus(429);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + msg + "\"}");
        return false;
    }
}
