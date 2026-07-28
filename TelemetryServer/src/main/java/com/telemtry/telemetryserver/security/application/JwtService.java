package com.telemtry.telemetryserver.security.application;

import com.telemtry.telemetryserver.security.infrastructure.configuration.JwtConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final JwtConfiguration configuration;

    public JwtService(JwtConfiguration configuration) {
        this.configuration = configuration;
    }

    private Key getSigningKey(){

        byte[] keyBytes = Decoders.BASE64.decode(configuration.getKey());
        return Keys.hmacShaKeyFor(keyBytes);


    }

    public String generateToken(UserDetails userDetails){

        Map<String,Object> claims = new HashMap<>();

        claims.put(
                "roles",
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + configuration.getExpiration()))
                .signWith(getSigningKey())
                .claims(claims)
                .compact();
    }

    public String extractUsername(String token){

        return extractClaim(token,Claims::getSubject);

    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){

        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token).getPayload();

    }


    public boolean isTokenValid(String token, UserDetails userDetails){

        final String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token)
                .before(new Date());
    }

}
