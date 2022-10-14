package hr.kraljic.web.module.notification.exception;

import hr.kraljic.web.exception.generic.BadRequestApiException;
import hr.kraljic.web.util.MessagesUtil;

public class InvalidNotificationAccessTokenApiException extends BadRequestApiException {
    private static final String defaultMessage = "apiException.badRequest.invalidNotificationAccessToken";

    public InvalidNotificationAccessTokenApiException() {
        super(MessagesUtil.get(defaultMessage));
    }
}
