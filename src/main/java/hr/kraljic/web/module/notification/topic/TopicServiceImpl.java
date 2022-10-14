package hr.kraljic.web.module.notification.topic;

import hr.kraljic.web.module.notification.token.model.NotificationAccessToken;
import hr.kraljic.web.module.notification.token.NotificationAccessTokenService;
import hr.kraljic.web.module.notification.topic.model.TopicKeyDto;
import hr.kraljic.web.module.core.user.model.ApplicationUser;
import hr.kraljic.web.module.core.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final NotificationAccessTokenService notificationAccessTokenService;
    private final UserService userService;

    public TopicServiceImpl(TopicRepository topicRepository,
                            NotificationAccessTokenService notificationAccessTokenService,
                            UserService userService) {
        this.topicRepository = topicRepository;
        this.notificationAccessTokenService = notificationAccessTokenService;
        this.userService = userService;
    }

    @Override
    public List<TopicKeyDto> getSubscribedTopics(String notificationAccessToken) {
        NotificationAccessToken userToken = notificationAccessTokenService
                .validateUserToken(notificationAccessToken);

        ApplicationUser user = userToken.getUser();

        List<TopicKeyDto> topicKeyDtoList =
                topicRepository.findAllFromUserByUsername(user.getUsername())
                        .stream()
                        .map(TopicKeyDto::map)
                        .collect(Collectors.toList());

        return topicKeyDtoList;
    }
}
