package hr.kraljic.web.security.jwt;

import hr.kraljic.web.module.core.authentication.AccessTokenProvider;
import hr.kraljic.web.module.core.authentication.TokenType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Komponenta zasluzna za provjeru autentifikacije/autorizacije korisnika iz Jwt-a
 */
@Component
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final TokenType TOKEN_TYPE = TokenType.BEARER;

    private final AccessTokenProvider accessTokenProvider;

    public JwtFilter(AccessTokenProvider accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
    }

    /**
     * Filter za provjeru autentifikacije korisnika
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);

        // Provjera da li je Jwt prisutan i da li je ispravan
        if (StringUtils.hasText(jwt) && this.accessTokenProvider.validateToken(jwt)) {
            // Dohvati autentifikacijski objekt iz Jwt-a
            Authentication authentication = this.accessTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }


    /**
     * Izvlaci Jwt iz zaglavlja zahtjeva
     *
     * @param request
     * @return Jwt u base64 formatu
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_TYPE.getTokenType())) {
            return bearerToken
                    .substring(TOKEN_TYPE.getTokenType().length())
                    .strip();
        }

        return null;
    }
}
