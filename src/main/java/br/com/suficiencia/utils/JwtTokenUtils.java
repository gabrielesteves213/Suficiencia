package br.com.suficiencia.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JwtTokenUtils implements Serializable {

    private static final long serialVersionUID = -7137341187394251979L;
    public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60;
    public static final long JWT_TOKEN_VALIDITY_REMEMBER =  1000 * 24 * 7 * 60 * 60;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getIdUserFromToken(String token){return getClaimFromToken(token,Claims::getId);}

    public  String generateToken(Map<String, Object> userClaims, UserDetails userDetails, Boolean rememberMe, String id) {
        if(Objects.isNull(userClaims)) userClaims = new HashMap<>();
        return doGenerateToken(userClaims, userDetails.getUsername(), rememberMe, id);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = getUsernameFromToken(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, Boolean rememberMe, String id) {
        Date expiration;
        if(rememberMe){
            expiration = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_REMEMBER);
        }else{
            expiration = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY);
        }
        return Jwts.builder()
                .setClaims(claims)
                .setId(id)
                .setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, "Bearer").compact();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey("Bearer").parseClaimsJws(token).getBody();
    }

    public String validateTokenAndReturnWithoutBearer(String token) {
        if (token.startsWith("Bearer")) {
            return token.replace("Bearer ", "");
        }
        return token;
    }
}