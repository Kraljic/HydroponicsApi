package hr.kraljic.web.module.notification.firebase.messages;

import com.google.firebase.messaging.Message;

public class TokenKeyUpdateMessage {
    private static final String TOPIC_KEY_UPDATE_EVENT = "TOPIC_KEY_UPDATE_EVENT";

    public static Message getMessage() {
        Message.Builder builder = Message.builder();
        return builder.setTopic(TOPIC_KEY_UPDATE_EVENT)
                .putData("TOPIC_KEY_UPDATE_EVENT", "")
                .build();
    }
}
