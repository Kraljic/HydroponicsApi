package hr.kraljic.web.module.core.authentication;

import hr.kraljic.web.exception.user.UserNotFoundApiException;
import hr.kraljic.web.module.core.authentication.model.RefreshToken;
import hr.kraljic.web.module.core.authentication.repository.RefreshTokenRepository;
import hr.kraljic.web.module.core.user.repository.UserRepository;
import hr.kraljic.web.module.core.user.model.ApplicationUser;
import hr.kraljic.web.util.StringHash;
import hr.kraljic.web.util.RandomTokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Komponenta RefreshTokenProvider sluzi za manipulaciju i obnavljanje korisnickih sesija.
 */
@Component
public class RefreshTokenProvider {
    private final AccessTokenProvider accessTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RandomTokenGenerator refreshTokenGenerator;
    private final StringHash stringHash;
    private final int tokenValiditySeconds;

    public RefreshTokenProvider(
            @Value("${rt.token-validity-seconds}") int tokenValiditySeconds,
            AccessTokenProvider accessTokenProvider,
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            RandomTokenGenerator refreshTokenGenerator,
            StringHash stringHash
    ) {
        this.accessTokenProvider = accessTokenProvider;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.stringHash = stringHash;
        this.tokenValiditySeconds = tokenValiditySeconds;
    }

    public int getTokenValiditySeconds() {
        return tokenValiditySeconds;
    }

    /**
     * Kreira novi refresh token i pripadajuci access token za korisnika.
     *
     * @param username
     * @return Novo kreirani refresh token s pripadajucim access tokenom..
     */
    public RefreshToken generateRefreshToken(String username) {
        // Dohvati autoriziranog korisnika
        ApplicationUser applicationUser = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new UserNotFoundApiException(username));

        Authentication authentication = new UsernamePasswordAuthenticationToken(applicationUser, "",  applicationUser.getAuthorities());

        // Generiraj novi access token i refresh token
        String jwtToken = accessTokenProvider.createToken(authentication);
        String refreshToken = refreshTokenGenerator.newRandomToken();

        RefreshToken rt = new RefreshToken();
        // Postavi vlasnika refresh tokena
        rt.setUser(applicationUser);
        // Postavi refresh token i access token
        rt.setAccessTokenHash(stringHash.getHash(jwtToken));        // Save hashed value in DB
        rt.setAccessToken(jwtToken);                                // Not stored in db (@Transient)
        rt.setRefreshTokenHash(stringHash.getHash(refreshToken));   // Save hashed value in DB
        rt.setRefreshToken(refreshToken);                           // Not stored in db (@Transient)

        // Postavi tip tokena
        rt.setTokenType(TokenType.BEARER);

        // Postavi vrijeme stvaranja i vrijeme istjecanja tokena
        rt.setCreatedAt(LocalDateTime.now());
        rt.setExpiresAt(LocalDateTime.now().plusSeconds(this.tokenValiditySeconds));

        rt.setValid(true);

        // Spremi token u bazu
        rt = refreshTokenRepository.save(rt);

        return rt;
    }

    /**
     * Ponistava specificnu sesiju korisnika. *Napomena: Sesija ce biti u potpunosti
     * unistena tek kada pripadni access token istekne.
     *
     * @param refreshToken Refresh token koji se zeli ponistiti
     * @param jwtToken     Pripadajuci access token (moze biti istecen)
     */
    public void invalidateRefreshToken(String refreshToken, String jwtToken) {
        Optional<RefreshToken> dbRefreshToken = findRefreshToken(refreshToken, jwtToken);

        if (dbRefreshToken.isEmpty()) {
            return;
        }

        RefreshToken invalidateRefreshToken = dbRefreshToken.get();
        invalidateRefreshToken.setValid(false);

        refreshTokenRepository.save(invalidateRefreshToken);
    }

    /**
     * Ponisti sve 'sesije' korisnika. *Napomena: Sesija ce biti u potpunosti
     * unistena tek kada pripadni access token istekne.
     *
     * @param username Korisnik cije se sesije zele unistiti.
     */
    public void invalidateUserRefreshTokens(String username) {
        List<RefreshToken> userRefreshTokens = refreshTokenRepository.findByUser_Username(username);

        userRefreshTokens.forEach(refreshToken -> {
            refreshToken.setValid(false);
            refreshTokenRepository.save(refreshToken);
        });
    }

    /**
     * Provjerava da li su refresh token i access token ispravni.
     *
     * @param refreshToken Refresh token koji se zeli provjeriti da li je ispravan.
     * @param jwtToken     Pripadajuci access token (moze biti istecen)
     * @return {@code true} ako su refresh token i access token ispravni.
     */
    public boolean validateRefreshToken(String refreshToken, String jwtToken) {
        Optional<RefreshToken> dbRefreshToken = findRefreshToken(refreshToken, jwtToken);

        // Provjeri da li refresh token postoji
        if (dbRefreshToken.isEmpty()) {
            return false;
        }

        // Provjeri da li je refresh token aktivan
        if (dbRefreshToken.get().isValid() == false) {
            return false;
        }

        // Provjeri da li je refresh token istekao
        if (dbRefreshToken.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            // Ako je istekao azuriraj mu stanje u bazi u neaktivan
            dbRefreshToken.get().setValid(false);
            refreshTokenRepository.save(dbRefreshToken.get());

            return false;
        }

        // Refresh token je ispravan
        return true;
    }

    /**
     * Trazi refresh token u bazi.
     *
     * @param refreshToken Refresh token u izvornom obliku (ne hash vrijednost)
     * @param jwtToken     Access token u izvornom obliku (ne hash vrijednost)
     * @return Refresh token zapis iz baze ili {@code Optional.empty()}
     */
    public Optional<RefreshToken> findRefreshToken(String refreshToken, String jwtToken) {
        // Izracunaj hash vrijednosti tokena (u bazu se sprema hash)
        String refreshTokenHash = stringHash.getHash(refreshToken);
        String jwtTokenHash = stringHash.getHash(jwtToken);

        // Dohvati refresh token iz baze prema hash vrijednostima
        Optional<RefreshToken> foundRefreshToken = refreshTokenRepository.findByRefreshTokenHashAndAccessTokenHash(refreshTokenHash, jwtTokenHash);

        return foundRefreshToken;
    }

}
