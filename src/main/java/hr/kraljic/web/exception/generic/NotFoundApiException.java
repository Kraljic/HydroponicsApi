package hr.kraljic.web.exception.generic;

import hr.kraljic.web.exception.ApiException;
import hr.kraljic.web.util.MessagesUtil;
import org.springframework.http.HttpStatus;

public class NotFoundApiException extends ApiException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String defaultMessage = "apiException.notFound";

    public NotFoundApiException() {
        this(MessagesUtil.get(defaultMessage));
    }
    public NotFoundApiException(String message) {
        super(message, HTTP_STATUS);
    }
}
