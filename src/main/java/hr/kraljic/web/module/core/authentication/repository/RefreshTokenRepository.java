package hr.kraljic.web.module.core.authentication.repository;

import hr.kraljic.web.module.core.authentication.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    /**
     * Pronalazi zapis o refresh i access tokenu iz bazi podataka
     *
     * @param refreshTokenHash hash vrijednost refresh tokena
     * @param jwtTokenHash hash vrijednost pripadnog access token refresh tokenu
     * @return
     */
    Optional<RefreshToken> findByRefreshTokenHashAndAccessTokenHash(String refreshTokenHash, String jwtTokenHash);

    /**
     * Pronalazi sve refresh token zapise za navedenog korisnika
     * @param username Korisnicko ime korisnika
     * @return Popis svih zapisa refresh tokena (i onih ne aktivnih/validnih)
     */
    List<RefreshToken> findByUser_Username(String username);

}
