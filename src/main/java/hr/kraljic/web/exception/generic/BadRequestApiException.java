package hr.kraljic.web.exception.generic;

import hr.kraljic.web.exception.ApiException;
import hr.kraljic.web.util.MessagesUtil;
import org.springframework.http.HttpStatus;

public class BadRequestApiException extends ApiException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String defaultMessage = "apiException.badRequest";

    public BadRequestApiException() {
        this(MessagesUtil.get(defaultMessage));
    }
    public BadRequestApiException(String message) {
        super(message, HTTP_STATUS);
    }
}