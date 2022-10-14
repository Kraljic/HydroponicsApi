package hr.kraljic.web.module.notification.token;

import hr.kraljic.web.module.notification.token.model.NotificationAccessToken;

public interface NotificationAccessTokenService {

    /**
     * Ispituje da li je token ispravan access token
     *
     * @param token Token koji se zeli provjeriti
     * @return Access token ako je token ispravan
     */
    NotificationAccessToken validateUserToken(String token);

    /**
     * Dohvaca token renutno prijavljenog korisnika, ako token ne postoji stvara se novi token
     *
     * @return
     */
    NotificationAccessToken getUserToken();

    /**
     * Stvara novi token i brise stari token ako postoji
     *
     * @return
     */
    NotificationAccessToken generateNewToken();
}
