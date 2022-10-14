package hr.kraljic.web.module.core.user.repository;

import hr.kraljic.web.module.core.user.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {
    /**
     * Pronadji korisnika s pripdanim korisnickim imenom
     *
     * @param username Korisnicko ime korisnika kojeg zelimo pronaci
     * @return
     */
    Optional<ApplicationUser> findOneByUsername(String username);

    /**
     * Pronadji sve korisnike koji pripadaju roli
     *
     * @param roleId Id role za koju zelimo pronaci korisnike
     * @return
     */
    @Query("SELECT distinct au FROM ApplicationUser au INNER JOIN au.roles r WHERE r.id = :roleId")
    List<ApplicationUser> findByRole(@Param("roleId") Integer roleId);

    /**
     * Provjeri postoji li korisnik s tim korisnickim imenom
     *
     * @param username Korisnicko ime koje zelimo provjeriti
     * @return {@code true} ako korisnicko ime vec postoji
     */
    boolean existsByUsername(String username);

    /**
     * Provjeri postoji li korisnik s tom email adresom
     *
     * @param email Email adresa koju zelimo provjeriti
     * @return {@code true} ako email adresa vec postoji
     */
    boolean existsByEmail(String email);
}
