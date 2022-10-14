package hr.kraljic.web.module.core.user.repository;

import hr.kraljic.web.module.core.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Dohvati rolu prema nazivu role
     *
     * @param roleName Naziv role
     * @return
     */
    Optional<Role> findByRole(String roleName);

    /**
     * Provjeri da li rola s tim imenom vec postoji u bazi
     *
     * @param roleName Naziv role koji se zeli provjeriti
     * @return {@true} ako rola s tim imenom vec postoji
     */
    boolean existsByRole(String roleName);
}
