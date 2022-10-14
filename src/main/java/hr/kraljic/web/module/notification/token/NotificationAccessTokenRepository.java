package hr.kraljic.web.module.notification.token;

import hr.kraljic.web.module.notification.token.model.NotificationAccessToken;
import hr.kraljic.web.module.core.user.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationAccessTokenRepository extends JpaRepository<NotificationAccessToken, Integer> {
    /**
     * Trazi access token prema poljima user i valid. Polje valid mora biti {@code true}.
     * Sluzi za pronalazenje korisnickog tokena.
     *
     * @param user Korisnik ciji se token zeli dohvatiti.
     * @return access token ako postoji
     */
    Optional<NotificationAccessToken> findByUserAndValidTrue(ApplicationUser user);

    List<NotificationAccessToken> findAllByUserAndValidTrue(ApplicationUser user);

    /**
     * Trazi access token prema poljima token i valid. Polje valid mora biti {@code true}.
     * Sluzi za validaciju tokena.
     *
     * @param token Token koji se ispituje da li je ispravan
     * @return access token ako je token validan
     */
    Optional<NotificationAccessToken> findByTokenAndValidTrue(String token);
}
