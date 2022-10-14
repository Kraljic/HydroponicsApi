package hr.kraljic.web.exception.user;

import hr.kraljic.web.exception.generic.BadRequestApiException;
import hr.kraljic.web.util.MessagesUtil;

public class UsernameInUseApiException extends BadRequestApiException {
    private static final String defaultMessage = "apiException.badRequest.usernameInUse";

    public UsernameInUseApiException(String username) {
        super(MessagesUtil.get(defaultMessage, username));
    }
}
