package hr.kraljic.web.module.hydroponics.device.model;

import hr.kraljic.web.config.AbstractAuditAware;
import hr.kraljic.web.module.notification.topic.model.Topic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(exclude = {"topic"})
public class Device extends AbstractAuditAware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String firmware;

    @OneToOne(mappedBy = "device", cascade = CascadeType.REMOVE)
    private Topic topic;
}
