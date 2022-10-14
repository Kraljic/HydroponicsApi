package hr.kraljic.web.module.notification;

import hr.kraljic.web.module.notification.topic.model.Topic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(exclude = {"topic"})
public class HydroponicNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime createdAt;
    private String message;
    private Boolean sent;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @ManyToOne
    @JoinColumn(name = "TOPIC_ID", nullable = false)
    private Topic topic;
}
