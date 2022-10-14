package hr.kraljic.web.exception.user;

import hr.kraljic.web.exception.generic.BadRequestApiException;
import hr.kraljic.web.util.MessagesUtil;

public class InvalidPasswordApiException extends BadRequestApiException {
    private static final String defaultMessage = "apiException.badRequest.invalidPassword";

    public InvalidPasswordApiException() {
        super(MessagesUtil.get(defaultMessage));
    }
}
