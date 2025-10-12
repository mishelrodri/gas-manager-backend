package com.mishelrodri.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtils {

    @Value("${jwt.sercret}")
    private String keyBase64;

    private SecretKey getSecretKey(){
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return key;
    }


    public String crearToken(String username){
        long ahora = System.currentTimeMillis();
        long treintaMinutos = java.util.concurrent.TimeUnit.HOURS.toMillis(8);

        return Jwts.builder()
                .subject(username)
                .signWith(getSecretKey())
                .issuedAt(new Date(ahora))
                .expiration(new Date(ahora + treintaMinutos))
                .compact();
    }


    public boolean verificarToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String obtenerUsuario(String token){
        try{
            Claims claims =   Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        }catch (Exception e){
            return null;
        }
    }

}
