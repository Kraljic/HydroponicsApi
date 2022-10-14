package hr.kraljic.web.module.notification.management.topic.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class TopicKeysResetEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public TopicKeysResetEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishTopicKeyResetEvent() {
        TopicKeysResetEvent topicKeysResetEvent = new TopicKeysResetEvent(this);
        applicationEventPublisher.publishEvent(topicKeysResetEvent);
    }
}
