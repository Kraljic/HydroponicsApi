package hr.kraljic.web.module.notification.topic;

import hr.kraljic.web.module.notification.topic.model.TopicKeyDto;

import java.util.List;

public interface TopicService {
    List<TopicKeyDto> getSubscribedTopics(String notificationAccessToken);
}
