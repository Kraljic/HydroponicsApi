package hr.kraljic.web.module.core.authentication.controller;

import hr.kraljic.web.exception.authentication.InvalidRefreshTokenApiException;
import hr.kraljic.web.module.core.authentication.RefreshTokenProvider;
import hr.kraljic.web.module.core.authentication.model.command.LoginCommand;
import hr.kraljic.web.module.core.authentication.model.command.RefreshTokenCommand;
import hr.kraljic.web.module.core.authentication.model.dto.JwtDto;
import hr.kraljic.web.module.core.authentication.service.AuthService;
import hr.kraljic.web.module.core.user.model.dto.UserDto;
import hr.kraljic.web.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;

/**
 * Kontroler za authetifikaciju korisnika
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    private final boolean httpsEnabled;
    private final AuthService authService;
    private final RefreshTokenProvider refreshTokenProvider;

    public AuthController(
            @Value("${https-enabled}") boolean httpsEnabled,
            AuthService authService,
            RefreshTokenProvider refreshTokenProvider
    ) {
        this.httpsEnabled = httpsEnabled;
        this.authService = authService;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    /**
     * Autentificira korisnika s korisnickim imenom i lozinkom te generira access token i refresh token.
     *
     * @param login Korisnicko ime i lozinka
     * @return Novi access token i refresh token
     */
    @PostMapping("/authenticate")
    public ResponseEntity<JwtDto> authenticate(@Valid @RequestBody LoginCommand login, HttpServletResponse response) {
        JwtDto tokenDto = authService.authenticate(login);

        Cookie refreshTokenCookie = generateRefreshTokenCookie(tokenDto.getRefreshToken());
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(tokenDto);
    }

    /**
     * Generira novi access token i refresh token te ponistava stari refresh token
     * kako se nebi mogao ponovo upotrijebiti.
     *
     * @param refreshTokenCommand Refresh token info
     * @return Novi access token i refresh token
     */
    @PostMapping("/refresh-token/mobile")
    public ResponseEntity<JwtDto> refresh(@Valid @RequestBody RefreshTokenCommand refreshTokenCommand) {
        JwtDto tokenDto = authService.refreshToken(
                refreshTokenCommand.getRefreshToken(),
                refreshTokenCommand.getAccessToken()
        );

        return ResponseEntity.ok(tokenDto);
    }

    /**
     * Generira novi access token i refresh token te ponistava stari refresh token
     * kako se nebi mogao ponovo upotrijebiti. Refresh token se sprema u http only cookie.
     *
     * @param request
     * @param response
     * @return Novi access token i refresh token
     */
    @PostMapping("/refresh-token/web")
    public ResponseEntity<JwtDto> refresh(HttpServletRequest request, HttpServletResponse response) {
        RefreshTokenCommand refreshTokenCommand = extractRefreshTokenCommand(request);

        JwtDto tokenDto = authService.refreshToken(
                refreshTokenCommand.getRefreshToken(),
                refreshTokenCommand.getAccessToken()
        );

        Cookie refreshTokenCookie = generateRefreshTokenCookie(tokenDto.getRefreshToken());
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(tokenDto);
    }

    /**
     * Ponisti refresh token, sesija ce biti unistena tek kada jwt token istekne.
     *
     * @param refreshTokenCommand
     */
    @PostMapping("/revoke-token/mobile")
    @ResponseStatus(HttpStatus.OK)
    public void revokeToken(@Valid @RequestBody RefreshTokenCommand refreshTokenCommand) {
        authService.revokeToken(
                refreshTokenCommand.getRefreshToken(),
                refreshTokenCommand.getAccessToken()
        );
    }

    /**
     * Ponisti refresh token, sesija ce biti unistena tek kada jwt token istekne.
     *
     * @param request
     */
    @PostMapping("/revoke-token/web")
    @ResponseStatus(HttpStatus.OK)
    public void revokeToken(HttpServletRequest request) {
        RefreshTokenCommand refreshTokenCommand = extractRefreshTokenCommand(request);
        authService.revokeToken(
                refreshTokenCommand.getRefreshToken(),
                refreshTokenCommand.getAccessToken()
        );
    }


    /**
     * Provjeri da li je Jwt ispravan, ako je ispravan vraca prijavljenog korisnika.
     *
     * @return {@code UserDto} ako je Jwt ispravan
     */
    @GetMapping("/validate-token")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getCurrentUser() {
        UserDto currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }

    /**
     * Vadi refresh token i access token iz zahtjeva te ih sprema u RefreshTokenCommand
     *
     * @param request
     * @return novi RefreshTokenCommand objekt ili null ako nije refresh ili access token nisu pronadjeni
     */
    private RefreshTokenCommand extractRefreshTokenCommand(HttpServletRequest request) {
        // Extract access token
        String bearerToken = request.getHeader(JwtFilter.AUTHORIZATION_HEADER);
        String tokenType = JwtFilter.TOKEN_TYPE.getTokenType();
        if (StringUtils.hasText(bearerToken) == false || bearerToken.startsWith(tokenType) == false) {
            throw new InvalidRefreshTokenApiException();
        }
        String accessToken = bearerToken.substring(tokenType.length()).strip();

        // Extract refresh token
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new InvalidRefreshTokenApiException();
        }
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(REFRESH_TOKEN_COOKIE_NAME))
                .findFirst()
                .map(c -> c.getValue())
                .orElse(null);

        // Check if they are present
        if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(refreshToken)) {
            throw new InvalidRefreshTokenApiException();
        }

        // Create new refresh token command object with extracted info
        RefreshTokenCommand refreshTokenCommand = new RefreshTokenCommand();
        refreshTokenCommand.setRefreshToken(refreshToken);
        refreshTokenCommand.setAccessToken(accessToken);

        return refreshTokenCommand;
    }

    /**
     * Stvara novi cookie za refresh token
     *
     * @param refreshToken
     * @return Cookie koji predstavlja refresh token
     */
    private Cookie generateRefreshTokenCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);

        refreshTokenCookie.setMaxAge(refreshTokenProvider.getTokenValiditySeconds());
        refreshTokenCookie.setSecure(httpsEnabled == true);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");

        return refreshTokenCookie;
    }
}
