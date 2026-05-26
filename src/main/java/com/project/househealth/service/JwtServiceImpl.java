package com.project.househealth.service;
import com.project.househealth.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService{

    @Value("${jwt.secret}")
    private String secretKey;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(
                secretKey.getBytes()
        );
    }

    @Override
    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role",
                        user.getSystemRole().name())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
                ).signWith(
                        getSigningKey(),
                        SignatureAlgorithm.HS256
                ).compact();
    }
}
