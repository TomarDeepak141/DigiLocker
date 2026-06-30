package Spring.digiLocker.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;
    public String generateToken(
            Long userId,
            String email,
            String role
    ) {

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email",email)
                .claim("role",role)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                +expiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }
    //used to extract all the claims
    private Claims extractAllClaims(
            String token
    ){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public Long extractUserId(String token) {
        return Long.valueOf(
                extractAllClaims(token)
                        .getSubject()
        );
    }
    public String extractEmail(String token) {
        return extractAllClaims(token)
                .get("email", String.class);
    }
    public String extractRole(String token) {
        return extractAllClaims(token)
                .get("role", String.class);
    }
    public boolean isTokenValid(
            String token
    ) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
