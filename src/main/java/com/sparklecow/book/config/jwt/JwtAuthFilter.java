package com.sparklecow.book.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(request.getServletPath().contains("/api/auth")){
            filterChain.doFilter(request, response);
            return;
        }
        if(jwtToken == null || !jwtToken.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        String token = jwtToken.substring(7);
        String username = jwtUtils.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(userDetails == null || !jwtUtils.validateToken(token, userDetails)){
            filterChain.doFilter(request, response);
            return;
        }
        if(SecurityContextHolder.getContext().getAuthentication() != null){
            filterChain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
