package hr.kraljic.web.exception.user;

import hr.kraljic.web.exception.generic.BadRequestApiException;
import hr.kraljic.web.util.MessagesUtil;

public class EmailInUseApiException extends BadRequestApiException {
    private static final String defaultMessage = "apiException.badRequest.emailInUse";

    public EmailInUseApiException(String email) {
        super(MessagesUtil.get(defaultMessage, email));
    }
}
