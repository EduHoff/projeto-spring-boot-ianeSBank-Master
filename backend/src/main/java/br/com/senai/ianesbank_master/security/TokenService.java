package br.com.senai.ianesbank_master.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenService {
    private final SecretKey key;
    private final long duration;

    public TokenService(@Value("${ianesbank.jwt.secret}") String secret, @Value("${ianesbank.jwt.expiration-minutes}") long minutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.duration = minutes * 60_000;
    }

    public String gerar(String login) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(login)
                .issuedAt(new Date(now))
                .expiration(new Date(now + duration))
                .signWith(key)
                .compact();
    }

    public String validar(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
