package gift.auth.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Collection;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    private final JwtProperties jwtProperties;
    private final SecretKey key;
    private final long ACCESS_TOKEN_VALIDITY_MILLIS;
    private final long REFRESH_TOKEN_VALIDITY_MILLIS;
    private final String ISSUER;

    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes());
        this.ACCESS_TOKEN_VALIDITY_MILLIS = jwtProperties.accessTokenValidity();
        this.REFRESH_TOKEN_VALIDITY_MILLIS = jwtProperties.refreshTokenValidity();
        this.ISSUER = jwtProperties.issuer();
    }

    public String createToken(Long userId, String email, Collection<String> roles) {
        Claims claims = Jwts.claims()
            .add("email", email)
            .add("roles", roles.stream().toList())
            .build();

        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY_MILLIS);

        return Jwts.builder()
            .issuer(ISSUER)
            .subject(userId.toString())
            .claims(claims)
            .issuedAt(now)
            .expiration(expiredAt)
            .signWith(key)
            .compact();
    }

    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY_MILLIS);

        return Jwts.builder()
            .issuer(ISSUER)
            .subject(userId.toString())
            .issuedAt(now)
            .expiration(expiredDate)
            .signWith(key)
            .compact();
    }

    public Long getUserId(String token) {
        return Long.parseLong(
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject()
        );
    }

    public String getEmail(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("email", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getAccessTokenExpirationTime() {
        return ACCESS_TOKEN_VALIDITY_MILLIS / 1000;
    }

    public long getRefreshTokenExpirationTime() {
        return REFRESH_TOKEN_VALIDITY_MILLIS / 1000;
    }

}
