package hr.kraljic.web.module.notification.topic.model;

import hr.kraljic.web.config.AbstractAuditAware;
import hr.kraljic.web.module.hydroponics.device.model.Device;
import hr.kraljic.web.module.core.user.model.ApplicationUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = {"device", "subscribedUsers"})
public class Topic extends AbstractAuditAware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String topicKey;

    @OneToOne
    @JoinColumn(name = "DEVICE_ID", nullable = false)
    private Device device;

    @ManyToMany(mappedBy = "topics", cascade = {CascadeType.PERSIST, CascadeType.DETACH}, fetch = FetchType.EAGER)
    private Set<ApplicationUser> subscribedUsers = new HashSet<>();

    public boolean addUser(ApplicationUser user) {
        user.getTopics().add(this);
        return this.getSubscribedUsers().add(user);
    }

    public boolean removeUser(ApplicationUser user) {
        user.getTopics().remove(this);
        return this.getSubscribedUsers().remove(user);
    }
}
