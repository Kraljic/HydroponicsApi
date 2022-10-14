package hr.kraljic.web.exception.generic;

import hr.kraljic.web.exception.ApiException;
import hr.kraljic.web.util.MessagesUtil;
import org.springframework.http.HttpStatus;

public class ForbiddenApiException extends ApiException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    private static final String defaultMessage = "apiException.forbidden";

    public ForbiddenApiException() {
        this(MessagesUtil.get(defaultMessage));
    }
    public ForbiddenApiException(String message) {
        super(message, HTTP_STATUS);
    }
}