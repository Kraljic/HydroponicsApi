package hr.kraljic.web.exception.user;

import hr.kraljic.web.exception.generic.ForbiddenApiException;
import hr.kraljic.web.util.MessagesUtil;

public class UserIsWriteProtectedApiException extends ForbiddenApiException {
    private static final String defaultMessage = "apiException.forbidden.writeProtectedUser";

    public UserIsWriteProtectedApiException(String username) {
        super(MessagesUtil.get(defaultMessage, username));
    }
}
