package com.brais.gymtrack.auth;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.brais.gymtrack.user.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

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
    
}
