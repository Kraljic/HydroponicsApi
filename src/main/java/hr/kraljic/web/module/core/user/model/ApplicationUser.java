package hr.kraljic.web.module.core.user.model;

import hr.kraljic.web.config.AbstractAuditAware;
import hr.kraljic.web.module.core.authentication.model.RefreshToken;
import hr.kraljic.web.module.notification.token.model.NotificationAccessToken;
import hr.kraljic.web.module.notification.topic.model.Topic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationUser extends AbstractAuditAware implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "USERNAME", length = 45, unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String username;

    @Column(name = "PASSWORD", length = 255, nullable = false)
    private String password;

    @Column(name = "EMAIL", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "ACTIVE", nullable = false)
    private boolean active = true;

    @Column(name = "WRITE_PROTECTED", nullable = false)
    private boolean writeProtected = false;

    @OneToOne(mappedBy = "user",
            fetch = FetchType.EAGER, optional = true, cascade = CascadeType.ALL)
    private Profile profile;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(
            name = "USER_ROLES",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"),}
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<RefreshToken> refreshTokens;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "USER_TOPICS",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "TOPIC_ID", referencedColumnName = "ID")}
    )
    private Set<Topic> topics = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<NotificationAccessToken> notificationAccessToken;

    /**
     * Provjeri ima li korisnik trazenu dozvolu
     *
     * @param authority Ime dozvole ili role (sa prefixom)
     * @return {@code true} ako korisnik ima trazenu dozvolu
     */
    public boolean hasAuthority(String authority) {
        return getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(authority));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        roles.forEach(r -> {
            r.getAuthorities()
                    .stream()
                    .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                    .forEach(authorities::add);
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
