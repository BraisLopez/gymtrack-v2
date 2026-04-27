package com.brais.gymtrack.auth;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.brais.gymtrack.user.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;


    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(user.getEmail()) //Identifica al usuario
                .claim("userId", user.getUserId()) //Agrega el ID del usuario al token
                .claim("role", user.getRole().name()) //Agrega el rol del usuario al token
                .issuedAt(now) //Agrega la fecha de emisión al token
                .expiration(expiration) //Agrega la fecha de expiración al token
                .signWith(getSignInKey()) //Firma el token con la clave secreta
                .compact(); //Genera el token y lo devuelve como una cadena
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token){
        return extractAllClaims(token).getSubject();
    }


    private boolean isTokenExpired(String token){
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public boolean isTokenValid(String token, User user){
        String email = extractEmail(token);
        return email.equals(user.getEmail()) && !isTokenExpired(token);
    }
    
}
