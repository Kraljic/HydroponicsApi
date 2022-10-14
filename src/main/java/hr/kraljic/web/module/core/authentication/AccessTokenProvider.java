package hr.kraljic.web.module.core.authentication;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Komponenta RefreshTokenProvider sluzi za manipulaciju pristupnih tokena, odnosno Jwt-a.
 */
@Component
public class AccessTokenProvider {
    private final Logger log = LoggerFactory.getLogger(AccessTokenProvider.class);

    private static final String AUTHORITIES_KEY = "authorities";

    private final Key key;

    private final long tokenValidityInMilliseconds;

    public AccessTokenProvider(
            @Value("${jwt.token-validity-seconds}") int tokenValiditySeconds,
            @Value("${jwt.base64-secret}") String base64Secret
    ) {
        byte[] keyBytes;
        keyBytes = Decoders.BASE64.decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds = 1000 * tokenValiditySeconds;
    }

    /**
     * Stvara novi access token (jwt) za autoriziranog korisnika.
     *
     * @param authentication
     * @return Jwt u kodiran u base64 formatu
     */
    public String createToken(Authentication authentication) {
        // Pretvori listu dozvola u polje znakova, svaka dozvola odvojena je , (zarezom)
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Izracunaj vrijeme istjecanja Jwt tokena
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        // Izradi novi Jwt
        return Jwts
                .builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * Dohvati autoriziranog korisnika iz Jwt-a
     *
     * @param token Kodirani Jwt u base64 formatu
     * @return Autorizacijski tokene s podacima korisnika
     */
    public Authentication getAuthentication(String token) {
        // Dohvati podatke Jwt-a
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        // Dohvati dozvole iz Jwt-a
        String authoritiesString = claims.get(AUTHORITIES_KEY).toString();
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
        if (StringUtils.hasText(authoritiesString)) {
            authorities = Arrays
                    .stream(authoritiesString.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        // Dohvati korisnika iz Jwt-a
        User principal = new User(claims.getSubject(), "", authorities);

        // Vrati novi autorizcijski token
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * Provjeri da li je Jwt ispravan
     *
     * @param authToken Kodirani Jwt u base64 formatu
     * @return {@code true} ako je Jwt ispravan.
     */
    public boolean validateToken(String authToken) {
        try {
            // Baca JwtException ako je token neispravan
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }

    /**
     * Dohvati korisnicko ime iz isteklog jwt tokena.
     *
     * @param expiredJwt
     * @return
     */
    public String getUsernameFromExpiredToken(String expiredJwt) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(expiredJwt).getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        return claims.getSubject();
    }
}
