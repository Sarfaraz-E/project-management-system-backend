package com.example.ProjectManagementSystem.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtProvider {
    //copied from
    static SecretKey key =
            Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());


    public static String generateToken(Authentication authentication) {

        String jwt = Jwts.builder().setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86400000)) //24 hrs
                .claim("email", authentication.getName())
                .signWith(key)
                .compact();
        return jwt;
    }

    public static String getEmailFromToken(String jwt) {
        // 1. Add this check to remove the prefix!
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }

        // 2. Now parse the token safely
        SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();

        return String.valueOf(claims.get("email"));
    }
}
