package hr.kraljic.web.module.notification.exception;

import hr.kraljic.web.exception.generic.BadRequestApiException;
import hr.kraljic.web.util.MessagesUtil;

public class InvalidTopicIdListApiException extends BadRequestApiException {
    private static final String defaultMessage = "apiException.badRequest.topicInvalidIdList";

    public InvalidTopicIdListApiException() {
        super(MessagesUtil.get(defaultMessage));
    }
}
