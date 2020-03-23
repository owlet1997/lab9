package com.ncedu.user_manager.filter;

import com.ncedu.user_manager.model.JWTAuthenticationToken;
import com.ncedu.user_manager.model.security.UserPrincipal;
import com.ncedu.user_manager.model.security.UserRole;
import com.ncedu.user_manager.service.impl.JWTTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_PREFIX = "Bearer ";

    private final JWTTokenService tokenService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = httpServletRequest.getHeader(AUTH_HEADER);

        if (requestTokenHeader != null && requestTokenHeader.startsWith(AUTH_PREFIX)) {
            String jwtToken = requestTokenHeader.substring(AUTH_PREFIX.length());
            try {
                String username = tokenService.getUsernameFromToken(jwtToken);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(username);
                    if (tokenService.validateAccessToken(jwtToken, userPrincipal)) {
                        JWTAuthenticationToken token = new JWTAuthenticationToken(
                                userPrincipal, jwtToken, userPrincipal.getAuthorities()
                        );
                        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(token);
                   } else if (tokenService.validateRefreshToken(jwtToken, userPrincipal)) {
                        JWTAuthenticationToken token = new JWTAuthenticationToken(
                                userPrincipal, jwtToken, Collections.singleton(UserRole.REFRESH_ACCESS_ROLE)
                        );
                        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(token);
                    }
                }
            } catch (ExpiredJwtException e) {
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired");
                return;
            } catch (Exception e) {
                httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to get JWT Token");
                return;
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
