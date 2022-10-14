package hr.kraljic.web.module.notification.management.topic.event;

import hr.kraljic.web.module.core.user.model.ApplicationUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserTopicsChangedEvent extends ApplicationEvent {
    private final ApplicationUser user;

    public UserTopicsChangedEvent(Object source, ApplicationUser user) {
        super(source);
        this.user = user;
    }
}
