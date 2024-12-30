package com.security.security.JWT;

import com.security.security.entity.user;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class Jwtservice {
    private String secretkey;
    public Jwtservice() {
        // Encode the secret key as a Base64 string for safe storage.
      this.secretkey = Base64.getEncoder().encodeToString("KeyisvVivekAManenverngiuedwgalsnceofjwguc22oh4tuhfrghvoin".getBytes(StandardCharsets.UTF_8));
    }
    //JWTs allow you to embed additional information (claims) about the user or entity the token represents.
    public String generatetoken(user u) {
        Map<String,String> Claims = new HashMap<>();
        Claims.put("role",u.getRole());//adding one more custom claim in token
        return Jwts.builder()
                .claims()
                .add(Claims)
                .subject(u.getUserName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60 * 60 *10))
                .and()
                .signWith(getkey())
                .compact();
                

    }
//create a key
// Generate the cryptographic key for signing the JWT
  private Key getkey(){
        byte[] keybyte = Decoders.BASE64.decode(secretkey);
      // Use the byte array to create an HMAC key suitable for HS256 algorithm
        return Keys.hmacShaKeyFor(keybyte);
  }


    public String extractusername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims,T> ClaimResolver) {
        Claims claims =extractClaims(token);
        return ClaimResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
       return Jwts.parser()
               .verifyWith((SecretKey) getkey())
               .build()
               .parseSignedClaims(token)
               .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userdetails) {
        final String userName = extractusername(token);
        return (userName.equals(userdetails.getUsername()) && isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token , Claims::getExpiration);
    }
}
//success/