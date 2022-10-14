package hr.kraljic.web.exception.generic;

import hr.kraljic.web.exception.ApiException;
import hr.kraljic.web.util.MessagesUtil;
import org.springframework.http.HttpStatus;

public class UnauthorizedApiException extends ApiException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;
    private static final String defaultMessage = "apiException.unauthorized";

    public UnauthorizedApiException() {
        this(MessagesUtil.get(defaultMessage));
    }
    public UnauthorizedApiException(String message) {
        super(message, HTTP_STATUS);
    }
}