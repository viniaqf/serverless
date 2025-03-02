package com.example.serverless.serverless.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    public String extractUsername(String token) {
        log.debug("Extraindo username do token");
        String username = extractClaim(token, Claims::getSubject);
        log.debug("Username extraído: {}", username);
        return username;
    }
    
    public String generateToken(UserDetails userDetails) {
        log.debug("Gerando token para usuário: {}", userDetails.getUsername());
        return generateToken(new HashMap<>(), userDetails);
    }
    
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        log.info("Gerando token para usuário: {} com claims extras", userDetails.getUsername());
        try {
            String token = Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            log.debug("Token gerado com sucesso");
            return token;
        } catch (Exception e) {
            log.error("Erro ao gerar token: {}", e.getMessage());
            throw e;
        }
    }
    
    public boolean isTokenValid(String token, UserDetails userDetails) {
        log.debug("Validando token para usuário: {}", userDetails.getUsername());
        try {
            final String username = extractUsername(token);
            boolean isValid = (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
            log.debug("Token é válido: {}", isValid);
            return isValid;
        } catch (Exception e) {
            log.error("Erro ao validar token: {}", e.getMessage());
            return false;
        }
    }
    
    private boolean isTokenExpired(String token) {
        log.trace("Verificando expiração do token");
        boolean isExpired = extractExpiration(token).before(new Date());
        log.debug("Token expirado: {}", isExpired);
        return isExpired;
    }
    
    private Date extractExpiration(String token) {
        log.trace("Extraindo data de expiração do token");
        return extractClaim(token, Claims::getExpiration);
    }
    
    private Claims extractAllClaims(String token) {
        log.trace("Extraindo todas as claims do token");
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.trace("Claims extraídas com sucesso");
            return claims;
        } catch (Exception e) {
            log.error("Erro ao extrair claims do token: {}", e.getMessage());
            throw e;
        }
    }
    
    private Key getSignInKey() {
        log.trace("Gerando chave de assinatura");
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            log.error("Erro ao gerar chave de assinatura: {}", e.getMessage());
            throw e;
        }
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        log.trace("Extraindo claim específica do token");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
}