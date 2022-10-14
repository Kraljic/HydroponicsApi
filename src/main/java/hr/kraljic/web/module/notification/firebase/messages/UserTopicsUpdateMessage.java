package hr.kraljic.web.module.notification.firebase.messages;

import com.google.firebase.messaging.Message;
import hr.kraljic.web.module.notification.token.model.NotificationAccessToken;

public class UserTopicsUpdateMessage {
    public static Message getMessage(NotificationAccessToken notificationAccessToken) {
        Message.Builder builder = Message.builder();
        return builder.setTopic(notificationAccessToken.getToken())
                .putData(notificationAccessToken.getToken(), "")
                .build();
    }
}
