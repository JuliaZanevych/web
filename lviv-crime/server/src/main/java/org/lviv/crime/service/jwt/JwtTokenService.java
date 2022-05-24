package org.lviv.crime.service.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.lviv.crime.dto.auth.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JwtTokenService {

    private static final String AUTH_ISSUER = "Lviv crime";

    private static final String USER_DATA_CLAIMS = "user";

    private final ObjectMapper objectMapper;

    private final String jwtSigningKey;

    private final long authTokenLifetime;

    public JwtTokenService(ObjectMapper objectMapper, @Value("${JWT_SIGNING_KEY}") String jwtSigningKey,
                           @Value("${AUTH_TOKEN_LIFETIME}") int authTokenLifetime) {

        this.objectMapper = objectMapper;
        this.jwtSigningKey = jwtSigningKey;
        this.authTokenLifetime = 1000L * authTokenLifetime;
    }

    public User receiveUser(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSigningKey).parseClaimsJws(token).getBody();
        Map<String, Object> userDataMap = claims.get(USER_DATA_CLAIMS, Map.class);
        return objectMapper.convertValue(userDataMap, User.class);
    }

    public String generateToken(User user) {
        Claims claims = Jwts.claims();
        claims.put(USER_DATA_CLAIMS, user);

        long creationTime = System.currentTimeMillis();
        long expirationTime = creationTime + authTokenLifetime;

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(AUTH_ISSUER)
                .setIssuedAt(new Date(creationTime))
                .setExpiration(new Date(expirationTime))
                .signWith(SignatureAlgorithm.HS256, jwtSigningKey)
                .compact();
    }
}
