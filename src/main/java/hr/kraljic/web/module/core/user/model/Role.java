package hr.kraljic.web.module.core.user.model;

import hr.kraljic.web.config.AbstractAuditAware;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role extends AbstractAuditAware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "ROLE", unique = true, length = 45, nullable = false)
    @EqualsAndHashCode.Include
    private String role;

    @Column(name = "LOCKED", nullable = false)
    @EqualsAndHashCode.Include
    private Boolean locked;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ROLE_AUTHORITIES",
            joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")}
    )
    private Set<Authority> authorities;

}
