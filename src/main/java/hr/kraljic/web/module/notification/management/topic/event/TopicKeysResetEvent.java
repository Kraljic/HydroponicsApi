package hr.kraljic.web.module.notification.management.topic.event;

import org.springframework.context.ApplicationEvent;

public class TopicKeysResetEvent extends ApplicationEvent {
    public TopicKeysResetEvent(Object source) {
        super(source);
    }
}
