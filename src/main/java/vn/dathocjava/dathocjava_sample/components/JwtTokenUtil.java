package vn.dathocjava.dathocjava_sample.components;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import vn.dathocjava.dathocjava_sample.model.User;

import java.security.InvalidParameterException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private long expiration; // enviroment variable
    @Value("${jwt.secretKey}")
    private String secretKey;
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("userId",user.getId());
        try {
            String token = Jwts.builder()
                    .setClaims(claims) // extract claims
                    .setSubject(user.getEmail())
                    .setExpiration(new Date(System.currentTimeMillis()+ expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {
            throw  new InvalidParameterException("Cannot create token: "+e.getMessage());
            //return null;
        }
    }
    private Key getSignInKey(){
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    // check expiration
    public boolean isTokenExpired(String token){
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }
    public String extractEmail(String token){
        return  extractClaim(token, Claims::getSubject);
    }
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> {
            Object value = claims.get("userId");
            if (value instanceof Integer) {
                return ((Integer) value).longValue();
            } else if (value instanceof Long) {
                return (Long) value;
            } else {
                throw new IllegalStateException("Invalid userId type in token: " + value.getClass());
            }
        });
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String phone = extractEmail(token);
        return (phone.equals(userDetails.getUsername())
                && !isTokenExpired(token) ); // kiểm tra xem còn hạn hay không
    }

}
