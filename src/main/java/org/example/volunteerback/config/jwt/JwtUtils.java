package org.example.volunteerback.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.volunteerback.model.user.User;
import org.example.volunteerback.model.user.UserDetailsImpl;
import org.example.volunteerback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;


@Service
public class JwtUtils{

    @Value("${jwt.private.secret}")
    private String jwtPrivateSecret;

    private final String jwtPublicSecret =  "MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQBRzCZYB4AG9AXjVqe6KYjA/LLLpta\n" +
            "BmvN1K2sdL7J9vLIW6f6r0E/Fy+gcg2OhdHEmTRk2YSWlp7Xj51QzsQY4aUA3bae\n" +
            "FikxNcpxXEGV7PDcHl5nee0mZ+JHLCLiQp2hFN3sdSZ0j6BxXMenVQukHIRsVd//\n" +
            "oV/axqmfVJSLCiuuXgc=";

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

    public boolean validateJwt(String token) throws Exception {
        PublicKey publicKey = keyLoader.loadPublicKey(jwtPublicSecret);

        try {
            Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }





}
