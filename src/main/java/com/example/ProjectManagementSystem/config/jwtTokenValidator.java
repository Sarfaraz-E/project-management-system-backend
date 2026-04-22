//package com.example.ProjectManagementSystem.config;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.crypto.SecretKey;
//import java.io.IOException;
//import java.util.List;
//
//public class jwtTokenValidator extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String jwt = request.getHeader(JwtConstant.JWT_HEADER);
//
//        // Check if token exists AND starts with "Bearer "
//        if (jwt != null && jwt.startsWith("Bearer ")) {
//
//            // Remove "Bearer " prefix
//            jwt = jwt.substring(7);
//
//            try {
//                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
//
//                Claims claims = Jwts.parser()// Updated to parserBuilder()
//                        .setSigningKey(key)
//                        .build()
//                        .parseClaimsJws(jwt)
//                        .getBody();
//
//                String email = String.valueOf(claims.get("email"));
//                String authorities = String.valueOf(claims.get("authorities"));
//
//                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
//
//                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            } catch (Exception e) {
//                // If token is bad, throw exception
//                throw new BadCredentialsException("Invalid Token...");
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    // ---------------------------------------------------------
//    // THIS IS THE MISSING PIECE FIXING YOUR ERROR
//    // ---------------------------------------------------------
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        // Skip validation logic for login/signup endpoints
//        return request.getServletPath().startsWith("/auth");
//    }
//}



package com.example.ProjectManagementSystem.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class jwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = request.getHeader(JwtConstant.JWT_HEADER);
        System.out.println("1. Token received in filter: " + jwt);

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
            System.out.println("2. Token after removing Bearer: " + jwt);

            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

                // UPDATED to modern JJWT 0.12.6 syntax
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();

                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                System.out.println("3. Token parsed successfully for email: " + email);

                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // PRINT THE ACTUAL ERROR TO THE CONSOLE
                System.out.println("🚨 TOKEN PARSING FAILED: " + e.getMessage());
                e.printStackTrace();
                throw new BadCredentialsException("Invalid Token: " + e.getMessage());
            }
        } else {
            System.out.println("🚨 No valid Bearer token found in the header!");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().startsWith("/auth");
    }
}