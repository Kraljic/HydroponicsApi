package hr.kraljic.web.module.core.permission;

import hr.kraljic.web.module.core.permission.event.AuthorityInitDoneEventPublisher;
import hr.kraljic.web.module.core.user.model.Authority;
import hr.kraljic.web.module.core.user.repository.AuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class PermissionInitializer {
    private final Logger log = LoggerFactory.getLogger(PermissionInitializer.class);

    private final AuthorityRepository authorityRepository;
    private final AuthorityInitDoneEventPublisher authorityInitDoneEventPublisher;

    private Set<String> permissions;

    public PermissionInitializer(AuthorityRepository authorityRepository, AuthorityInitDoneEventPublisher authorityInitDoneEventPublisher) {
        this.authorityRepository = authorityRepository;
        this.authorityInitDoneEventPublisher = authorityInitDoneEventPublisher;
        this.permissions = new HashSet<>();
    }

    public void registerPermissions(String[] permissions) {
        for (String permission : permissions) {
            log.debug("Registering new permission (" + permission + ")");
            boolean permissionAdded = this.permissions.add(permission);

            if (permissionAdded == false) {
                log.error("Permission " + permission + " was already present in the set");
                log.error("Please check permissions for possible duplicates");
                throw new PermissionNotAddedException(permission);
            }
        }
    }

    @EventListener
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("Initializing authorities");

        final Set<Authority> authorities = new HashSet<>();
        for (var permission : permissions) {
            log.debug("Saving permission to database " + permission);
            Authority authority = createAuthorityIfNotFound(permission);
            authorities.add(authority);
        }

        authorityInitDoneEventPublisher.publishAuthorityInitDoneEvent(authorities);
    }


    private Authority createAuthorityIfNotFound(String name) {
        Optional<Authority> authorityOptional = authorityRepository.findByAuthority(name);
        if (authorityOptional.isPresent()) {
            return authorityOptional.get();
        }

        Authority authority = new Authority();
        authority.setAuthority(name);

        return authorityRepository.save(authority);
    }
}
