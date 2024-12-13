package org.example.volunteerback.config.jwt;

import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class KeyLoader {


    private String cleanBase64Key(String base64Key) {
        if (base64Key == null || base64Key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        String cleanedKey = base64Key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\n", "")
                .replace("\r", "")
                .replace(" ", "")
                .trim();
        if (!isValidBase64(cleanedKey)) {
            throw new IllegalArgumentException("Invalid Base64 key format");
        }
        return cleanedKey;
    }

    private boolean isValidBase64(String base64String) {
        try {
            Base64.getDecoder().decode(base64String);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public PrivateKey loadPrivateKey(String base64PrivateKey) throws Exception {
        String cleanedKey = cleanBase64Key(base64PrivateKey);
        byte[] keyBytes = Base64.getDecoder().decode(cleanedKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid key specification. Make sure the private key is in PKCS8 format.", e);
        }
    }

    public PublicKey loadPublicKey(String base64PublicKey) throws Exception {
        String cleanedKey = cleanBase64Key(base64PublicKey);
        byte[] keyBytes = Base64.getDecoder().decode(cleanedKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
