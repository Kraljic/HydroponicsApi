package hr.kraljic.web.exception.user;

import hr.kraljic.web.exception.generic.NotFoundApiException;
import hr.kraljic.web.util.MessagesUtil;

public class UserNotFoundApiException extends NotFoundApiException {
    private static final String defaultMessage = "apiException.notFound.applicationUser";

    public UserNotFoundApiException(String username) {
        super(MessagesUtil.get(defaultMessage, username));
    }
}
