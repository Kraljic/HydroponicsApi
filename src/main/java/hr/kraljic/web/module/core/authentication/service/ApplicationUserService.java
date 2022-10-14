package hr.kraljic.web.module.core.authentication.service;

import hr.kraljic.web.module.core.user.model.ApplicationUser;
import hr.kraljic.web.module.core.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public ApplicationUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Vraća korisnika iz baze podataka s traženim korisničkim imenom.
     *
     * @param username Korisničko ime korisnika.
     * @return Korisnik s traženim korisničkim imenom.
     * @throws UsernameNotFoundException Iznimka će se dogoditi ako korisnik s traženim korisničkim imenom nije pronađen.
     */
    @Override
    public ApplicationUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOneByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(username));
    }
}
