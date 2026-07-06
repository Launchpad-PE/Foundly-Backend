package com.foundly.foundlyplatform.iam.infrastructure.authorization.sfs.pipeline;

import com.foundly.foundlyplatform.iam.infrastructure.authorization.sfs.model.UsernamePasswordAuthenticationTokenBuilder;
import com.foundly.foundlyplatform.iam.infrastructure.tokens.jwt.BearerTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class BearerAuthorizationRequestFilter extends OncePerRequestFilter {

    private final BearerTokenService tokenService;
    private final UserDetailsService userDetailsService;

    public BearerAuthorizationRequestFilter(BearerTokenService tokenService, @Qualifier("defaultUserDetailsService") UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ PERMITIR endpoints públicos SIN autenticación
        if (path.startsWith("/api/v1/authentication/") ||
                path.startsWith("/v3/api-docs/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/swagger-resources/") ||
                path.startsWith("/webjars/")) {
            log.info("🔓 Endpoint público: {}, saltando autenticación", path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authHeader = request.getHeader("Authorization");
            log.info("🔐 Authorization header recibido: {}", authHeader);

            String token = tokenService.getBearerTokenFrom(request);
            log.info("🔐 Token extraído: {}", token != null ? token.substring(0, Math.min(token.length(), 20)) + "..." : "null");

            if (token != null && tokenService.validateToken(token)) {
                String username = tokenService.getUsernameFromToken(token);
                log.info("🔐 Username extraído del token: {}", username);
                var userDetails = userDetailsService.loadUserByUsername(username);
                SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationTokenBuilder.build(userDetails, request));
                log.info("✅ Autenticación establecida para: {}", username);
            } else {
                log.warn("❌ Token inválido o no presente");
            }

        } catch (Exception e) {
            log.error("❌ Cannot set user authentication: {}", e.getMessage(), e);
        }
        filterChain.doFilter(request, response);
    }
}
