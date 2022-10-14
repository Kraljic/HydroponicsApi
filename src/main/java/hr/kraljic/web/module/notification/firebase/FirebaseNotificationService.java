package hr.kraljic.web.module.notification.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import hr.kraljic.web.module.notification.HydroponicNotification;
import hr.kraljic.web.module.notification.NotificationRepository;
import hr.kraljic.web.module.notification.NotificationService;
import hr.kraljic.web.module.notification.firebase.messages.TokenKeyUpdateMessage;
import hr.kraljic.web.module.notification.firebase.messages.UserTopicsUpdateMessage;
import hr.kraljic.web.module.notification.management.topic.event.TopicKeysResetEvent;
import hr.kraljic.web.module.notification.management.topic.event.UserTopicsChangedEvent;
import hr.kraljic.web.module.notification.token.NotificationAccessTokenRepository;
import hr.kraljic.web.module.notification.token.model.NotificationAccessToken;
import hr.kraljic.web.module.core.user.model.ApplicationUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service("FirebaseNotification")
public class FirebaseNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationAccessTokenRepository notificationAccessTokenRepository;
    private final String firebaseConfigPath;

    public FirebaseNotificationService(
            NotificationRepository notificationRepository,
            NotificationAccessTokenRepository notificationAccessTokenRepository,
            @Value("${app.firebase-configuration-file}") String firebaseConfigPath) {
        this.notificationRepository = notificationRepository;
        this.notificationAccessTokenRepository = notificationAccessTokenRepository;
        this.firebaseConfigPath = firebaseConfigPath;

    }

    @PostConstruct
    public void initialize() throws IOException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    private void send(Message message) throws ExecutionException, InterruptedException {
        FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    @Override
    public void sendNotification(HydroponicNotification hydroponicNotification) {
        // Osigurava da se obavjest posalje samo jednom
        if (hydroponicNotification.getSent() == true) {
            return;
        }

        String topicKey = hydroponicNotification.getTopic().getTopicKey();

        Notification firebaseNotification = Notification.builder()
                .setBody(hydroponicNotification.getMessage())
                .build();

        Message message = Message.builder()
                .setTopic(topicKey)
                .setNotification(firebaseNotification)
                .build();

        try {
            send(message);

            hydroponicNotification.setSent(true);
            notificationRepository.save(hydroponicNotification);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @EventListener
    public void onApplicationEvent(UserTopicsChangedEvent userTopicsChangedEvent) {
        ApplicationUser user = userTopicsChangedEvent.getUser();
        Optional<NotificationAccessToken> token = notificationAccessTokenRepository.findByUserAndValidTrue(user);

        if (token.isEmpty()) {
            return;
        }

        try {
            this.send(UserTopicsUpdateMessage.getMessage(token.get()));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @EventListener
    public void onApplicationEvent(TopicKeysResetEvent topicKeysResetEvent) {
        try {
            this.send(TokenKeyUpdateMessage.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
