package org.tokio.spring.securityjwt.core.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements InitializingBean {
    @Value(value = "${security.jwt.expiration-time:3600000}")// ThisIsAValidSecretKeyWith32Chars!
    private long jwtExpiration = 3600000; // 1*60*60 = 1 ho

    // Generar una clave secreta segura
    @Value(value = "${security.jwt.secret-key:VGhpc0lzQVZhbGlkU2VjcmV0S2V5V2l0aDMyQ2hhcnMh}")// ThisIsAValidSecretKeyWith32Chars!
    private String secret; // Clave secreta codificada en Base64

    // Decodificar y convertir en SecretKey
    private SecretKey secretKey;

    // Implementación de afterPropertiesSet
    @Override
    public void afterPropertiesSet() throws Exception {
        // secretKey = (SecretKey) getSignInKey();
        secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));

    }

    // Generar un JWT Token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username);
    }

    // Validar y extraer Claims del token
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // Configurar la clave para validar la firma
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Verificar si el token está expirado
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Validar el token comparando el username
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractAllClaims(token).getSubject();
        return username.equals(extractedUsername) && !isTokenExpired(token);
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        String extractedUsername = extractAllClaims(token).getSubject();
        return Objects.equals(extractedUsername,userDetails.getUsername()) && !isTokenExpired(token);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        //SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey) // Nueva forma de firmar
                .compact();
    }
    public String getUserName(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDate(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSignInKey(String base64Key) {
        byte[] keyBytes = Decoders.BASE64.decode(base64Key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private byte[] generateSecretBase64(){
        // Generar una clave segura para HS512 (256 bits o más)
        byte[] keyBytes = new byte[64]; // 512 bits
        new java.security.SecureRandom().nextBytes(keyBytes);

        // encode
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(keyBytes);
    }

    private byte[] generateSecretBase64(String keySecreteString){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(keySecreteString.getBytes());
    }
}
