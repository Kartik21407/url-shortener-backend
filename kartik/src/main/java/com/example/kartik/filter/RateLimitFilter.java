package com.example.kartik.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter implements Filter {

    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> timeStamps = new ConcurrentHashMap<>();

    private static final int MAX_REQUESTS = 5;
    private static final long TIME_WINDOW = 60000;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/h2-console")) {
            chain.doFilter(request, response);
            return;
        }

        String clientIp = req.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        timeStamps.putIfAbsent(clientIp, currentTime);
        requestCounts.putIfAbsent(clientIp, new AtomicInteger(0));

        if (currentTime - timeStamps.get(clientIp) > TIME_WINDOW) {
            timeStamps.put(clientIp, currentTime);
            requestCounts.get(clientIp).set(0);
        }

        if (requestCounts.get(clientIp).incrementAndGet() > MAX_REQUESTS) {
            res.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            res.getWriter().write("Too many requests. Please try again after a minute.");
            return;
        }

        chain.doFilter(request, response);
    }
}