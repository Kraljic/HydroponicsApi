package hr.kraljic.web.module.core.user;

import hr.kraljic.web.module.core.permission.event.AuthorityInitDoneEvent;
import hr.kraljic.web.module.core.user.model.ApplicationUser;
import hr.kraljic.web.module.core.user.model.Authority;
import hr.kraljic.web.module.core.user.model.Role;
import hr.kraljic.web.module.core.user.repository.RoleRepository;
import hr.kraljic.web.module.core.user.repository.UserRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Configuration
@ConfigurationProperties(prefix = "administrator")
@Data
public class AdministratorInit {
    private final Logger log = LoggerFactory.getLogger(AdministratorInit.class);

    private boolean init;
    private String username;
    private String password;
    private String email;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdministratorInit(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @EventListener
    @Transactional
    public void onApplicationEvent(AuthorityInitDoneEvent authorityInitDoneEvent) {
        Set<Authority> authorities = authorityInitDoneEvent.getAuthorities();
        if (init == false) {
            log.info("Skipping administrator initialization");
            return;
        }

        Optional<ApplicationUser> userOptional = userRepository.findOneByUsername(username);

        ApplicationUser administrator;
        if (userOptional.isPresent()) {
            administrator = userOptional.get();
            log.info("Administrator account found");
        } else {
            administrator = new ApplicationUser();
            log.info("Creating new administrator account");
        }

        administrator.setUsername(username);
        administrator.setEmail(email);
        administrator.setPassword(passwordEncoder.encode(password));
        administrator.setWriteProtected(true);
        administrator.setActive(true);

        Role role_admin = createRoleIfNotFound("ROLE_ADMIN", authorities, true);
        log.info("Role ROLE_ADMIN updated");

        if (administrator.getRoles() == null) {
            administrator.setRoles(new HashSet<>());
        }
        administrator.getRoles().add(role_admin);

        userRepository.save(administrator);

        log.info("Administrator account was successfully initialized");
    }

    Role createRoleIfNotFound(String name, Set<Authority> authorities, boolean locked) {
        Optional<Role> roleOptional = roleRepository.findByRole(name);

        Role role;
        if (roleOptional.isPresent()) {
            role = roleOptional.get();
            log.info("Role ROLE_ADMIN found");
        } else {
            role = new Role();
            log.info("Creating new ROLE_ADMIN");
        }

        role.setRole(name);
        role.setAuthorities(authorities);
        role.setLocked(locked);

        return roleRepository.save(role);
    }

}
