package hr.kraljic.web.module.core.user.model;

import hr.kraljic.web.config.AbstractAuditAware;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Profile extends AbstractAuditAware {
    @Id
    private Integer id;

    @Column(name = "FIRST_NAME", length = 60)
    private String firstName;

    @Column(name = "LAST_NAME", length = 60)
    private String lastName;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "USER_ID", nullable = true)
    private ApplicationUser user;
}
