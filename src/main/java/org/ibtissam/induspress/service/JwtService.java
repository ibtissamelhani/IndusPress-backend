package org.ibtissam.induspress.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.ibtissam.induspress.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET = "3F5E7A1D9C8B3E9A57F8C27D9B1A6D3C2B9C6F7E4D1A6E0F0B4A2F8B7A6E1C3F";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaim(token, claims -> claims.get("id", String.class)));
    }

    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(),userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(
            Map<String, Object> claims,
            UserDetails userDetails
    ) {
        UUID userId = ((User) userDetails).getId();

        String role = userDetails.getAuthorities().stream()
                .map(Object::toString)
                .filter(authority -> authority.startsWith("ROLE_"))
                .findFirst()
                .orElse("");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("id", userId.toString())
                .claim("role", ((User) userDetails).getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
