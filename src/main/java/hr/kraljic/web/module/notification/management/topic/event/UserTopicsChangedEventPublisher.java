package hr.kraljic.web.module.notification.management.topic.event;

import hr.kraljic.web.module.core.user.model.ApplicationUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class UserTopicsChangedEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserTopicsChangedEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishUserTopicsChangedEvent(ApplicationUser user) {
        UserTopicsChangedEvent userTopicsChangedEvent = new  UserTopicsChangedEvent(this, user);
        applicationEventPublisher.publishEvent(userTopicsChangedEvent);
    }
}
