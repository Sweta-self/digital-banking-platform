package com.banking.digital_banking_platform.security.config;

import com.banking.digital_banking_platform.security.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    //secretKey creation
    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(String email, Role role){
        return Jwts.builder()
                .subject(email)
                .claim("role",role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token){
        return getClaims(token).getSubject();
    }
    public String extractRole(String token){
        return getClaims(token).get("role",String.class);
    }
    public boolean validateToken(String token){
       try{
           Claims claims=getClaims(token);
           String email=claims.getSubject();
           String role=claims.get("role",String.class);
           return email !=null
                   && role!=null
                   && !isTokenExpired(token);
       }
       catch(Exception e){
           return false;
       }
    }

    private  boolean isTokenExpired(String token){
        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }
    //claims parsing
    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
