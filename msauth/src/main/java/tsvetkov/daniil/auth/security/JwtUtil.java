package tsvetkov.daniil.auth.security;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;
    private static final String USER_ID = "user_id";
    private static final String USERNAME = "username";
    private static final String ROLES = "roles";
    private static final String TOKEN_TYPE = "token_type";

    public String generateAccessToken(ReaderDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID, userDetails.getReader().getId());
        claims.put(USERNAME, userDetails.getUsername());
        claims.put(ROLES, userDetails.getAuthorities());
        claims.put(TOKEN_TYPE, TokenType.ACCESS);
        return createToken(claims, userDetails.getUsername(), accessTokenExpiration);
    }

    public String generateRefreshToken(ReaderDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID, userDetails.getReader().getId());
        claims.put(USERNAME, userDetails.getUsername());
        claims.put(ROLES, userDetails.getAuthorities());
        claims.put(TOKEN_TYPE, TokenType.REFRESH);
        return createToken(claims, userDetails.getUsername(), accessTokenExpiration * 400);
    }


    private String createToken(Map<String, Object> claims, String subject, Long tokenExpiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }


    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build().parseClaimsJws(token)
                .getBody()
                .get(USERNAME, String.class);
    }

    public Long extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build().parseClaimsJws(token)
                .getBody()
                .get(USER_ID, Long.class);
    }

    public TokenType extractTokenType(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build().parseClaimsJws(token)
                .getBody()
                .get(TOKEN_TYPE, TokenType.class);
    }


    public boolean validateAccessToken(String token, UserDetails userDetails) {
        TokenType tokenType = extractTokenType(token);
        if (tokenType != TokenType.ACCESS) {
            return false;
        }
        return validateTokenCommon(token, userDetails);
    }

    public boolean validateRefreshToken(String token, UserDetails userDetails) {
        TokenType tokenType = extractTokenType(token);
        if (tokenType != TokenType.REFRESH) {
            return false;
        }
        return validateTokenCommon(token, userDetails);
    }

    private boolean validateTokenCommon(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        if (userDetails instanceof ReaderDetails readerDetails) {
            if (!readerDetails.getReader().getId().equals(extractUserId(token))) {
                return false;
            }
        }
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "HmacSHA512");
    }
}
