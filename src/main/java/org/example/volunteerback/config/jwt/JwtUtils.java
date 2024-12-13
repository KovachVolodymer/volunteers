package org.example.volunteerback.config.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.example.volunteerback.model.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

@Slf4j
@Service
public class JwtUtils{

    @Value("${jwt.private.secret}")
    private String jwtPrivateSecret;

    private final String jwtPublicSecret =  "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0oR0SOEfADn4jTvhw+2+\n" +
            "ijgnecYzqizXuM6DlkCUTrDYIr2ou9hE585duJnzImA3GtBNBr7f80lnoUNkaBcE\n" +
            "QwAFwBE319KilfYU7xqmqH3k1Dfbaze+JB1T3zn+/eMNf1MOzVQ55XbmJOc7yH9T\n" +
            "ZEdoTC+RLioYR/lW1LMUuhYFsvXH44E1WBQrcrOkml+r25u0mmMrFyqagFWlOyLz\n" +
            "DnfkdoC7tZkRrDUa2bX7bNGYNHXED/bc9cCMD5q5aChS8MK0ToXGtxaIyP907Nbp\n" +
            "CAY40pi4tWOSj1wGgjT17a7EGrhpdJCClpXVJ2Adm96cIG6zJOIbk4oH16EBBJSK\n" +
            "TQIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    @Value("${kovach.app.jwtExpirationMs}")
    private Integer jwtExpirationMs;

    private final KeyLoader keyLoader;

    public JwtUtils(KeyLoader keyLoader) {
        this.keyLoader = keyLoader;
    }

    public String generateJwtToken(Authentication authentication) throws Exception {

        PrivateKey privateKey = keyLoader.loadPrivateKey(jwtPrivateSecret);

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        Claims claims = Jwts.claims();
        claims.put("id", userPrincipal.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(privateKey, SignatureAlgorithm.RS512)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) throws Exception {
        PublicKey publicKey = keyLoader.loadPublicKey(jwtPublicSecret);

        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean validateJwt(String token) throws Exception {
        PublicKey publicKey = keyLoader.loadPublicKey(jwtPublicSecret);

        try {
            Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }





}
