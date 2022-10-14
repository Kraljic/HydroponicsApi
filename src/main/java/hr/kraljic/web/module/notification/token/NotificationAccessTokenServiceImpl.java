package hr.kraljic.web.module.notification.token;

import hr.kraljic.web.module.notification.exception.InvalidNotificationAccessTokenApiException;
import hr.kraljic.web.module.notification.token.model.NotificationAccessToken;
import hr.kraljic.web.module.core.user.model.ApplicationUser;
import hr.kraljic.web.module.core.user.service.UserService;
import hr.kraljic.web.util.RandomTokenGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationAccessTokenServiceImpl implements NotificationAccessTokenService {
    private final NotificationAccessTokenRepository notificationAccessTokenRepository;
    private final RandomTokenGenerator notificationAccessTokenGenerator;
    private final UserService userService;

    public NotificationAccessTokenServiceImpl(NotificationAccessTokenRepository notificationAccessTokenRepository,
                                              RandomTokenGenerator notificationAccessTokenGenerator,
                                              UserService userService) {
        this.notificationAccessTokenRepository = notificationAccessTokenRepository;
        this.notificationAccessTokenGenerator = notificationAccessTokenGenerator;
        this.userService = userService;
    }

    @Override
    public NotificationAccessToken validateUserToken(String token) {
        return notificationAccessTokenRepository.findByTokenAndValidTrue(token)
                .orElseThrow(InvalidNotificationAccessTokenApiException::new);
    }

    @Override
    public NotificationAccessToken getUserToken() {
        ApplicationUser user = userService.getCurrentUser();

        return notificationAccessTokenRepository.findByUserAndValidTrue(user)
                .orElseGet(() -> generateNewToken(user));
    }

    @Override
    public NotificationAccessToken generateNewToken() {
        ApplicationUser user = userService.getCurrentUser();

        List<NotificationAccessToken> oldTokens = notificationAccessTokenRepository
                .findAllByUserAndValidTrue(user);

        oldTokens.forEach(t -> {
            t.setValid(false);
            notificationAccessTokenRepository.save(t);
        });

        return generateNewToken(user);
    }

    private NotificationAccessToken generateNewToken(ApplicationUser user) {
        String token = notificationAccessTokenGenerator.newRandomToken();

        NotificationAccessToken notificationAccessToken = new NotificationAccessToken();
        notificationAccessToken.setToken(token);
        notificationAccessToken.setUser(user);

        return notificationAccessTokenRepository.save(notificationAccessToken);
    }
}
