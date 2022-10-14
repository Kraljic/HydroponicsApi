package hr.kraljic.web.module.core.authentication.service;

import hr.kraljic.web.module.core.authentication.model.command.LoginCommand;
import hr.kraljic.web.module.core.authentication.model.dto.JwtDto;
import hr.kraljic.web.module.core.user.model.dto.UserDto;

/**
 * Servis za autentifikaciju korisnika
 */
public interface AuthService {

    /**
     * Authentificira korisnika prema korisnickom imenu i lozinki
     *
     * @param loginCommand Korisnicko ime i lozinka
     * @return Ako je autentifikaciji uspjesna vraca jwt i refresh token
     */
    JwtDto authenticate(LoginCommand loginCommand);

    /**
     * Generira novi refresh i access token
     *
     * @param oldRefreshToken
     * @param oldAccessToken
     * @return Ako je autentifikaciji uspjesna vraca jwt i refresh token
     */
    JwtDto refreshToken(String oldRefreshToken, String oldAccessToken);

    /**
     * Ponistava refresh token tako da se ne moze iskoristiti za dohvacanje novog
     *
     * @param oldRefreshToken refresh token koji se zeli ponistiti
     * @param oldAccessToken  access token koji pripada tom refresh tokenu
     */
    void revokeToken(String oldRefreshToken, String oldAccessToken);

    /**
     * Dohvaca prijavljenog korisnika i time validira ispravnost access tokena
     *
     * @return Informacije o prijavljenom korisniku
     */
    UserDto getCurrentUser();

}
