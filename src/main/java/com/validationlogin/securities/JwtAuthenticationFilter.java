package com.validationlogin.securities;

import com.validationlogin.services.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

    @Component
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

        @Autowired
        private AuthService authService;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            // Get the Authorization header
            String authorizationHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;

            // Check if the header exists and starts with "Bearer "
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7); // Extract token from "Bearer <token>"
                try {
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(authService.getSecretKey()) // Secret key to verify the JWT signature
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    username = claims.getSubject(); // Get username from token
                }catch (ExpiredJwtException e) {
                    logger.warn("the token is expired and not valid anymore", e);
                }
                catch (Exception e) {
                    // Log error or handle exceptions
                    e.printStackTrace();
                }
            }

            // If the token is valid and the username is not null, set authentication in context
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response); // Continue the request processing
        }
    }





