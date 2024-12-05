package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtils {
    private  final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private  final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public  String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmailId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }


    public  Claims validateToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public  boolean validateTokenForUser( User user,String token) {
        try {
            Claims claims = validateToken(token);
            String emailId = claims.getSubject();
            return emailId.equals(user.getEmailId());
        } catch (JwtException e) {
            return false;
        }
    }

    public  boolean validateTokenForUserRole(User user,String token, AccessRole access_role) {
        try {
            Claims claims = validateToken(token);
            String emailId = claims.getSubject();
            return (emailId.equals(user.getEmailId()) && (access_role.equals(user.getRole())) || user.getRole().equals(AccessRole.super_admin));
        } catch (JwtException e) {
            return false;
        }
    }
}

