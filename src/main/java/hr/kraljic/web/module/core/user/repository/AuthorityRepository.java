package hr.kraljic.web.module.core.user.repository;

import hr.kraljic.web.module.core.user.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    Optional<Authority> findByAuthority(String authorityName);

}
