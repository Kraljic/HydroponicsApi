package hr.kraljic.web.exception.authentication;

import hr.kraljic.web.exception.generic.UnauthorizedApiException;
import hr.kraljic.web.util.MessagesUtil;

public class InvalidRefreshTokenApiException extends UnauthorizedApiException {
    private static final String defaultMessage = "apiException.unauthorized.invalidRefreshToken";

    public InvalidRefreshTokenApiException() {
        super(MessagesUtil.get(defaultMessage));
    }
}
