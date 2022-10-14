package hr.kraljic.web.exception.generic;

import hr.kraljic.web.exception.ApiException;
import hr.kraljic.web.util.MessagesUtil;
import org.springframework.http.HttpStatus;

public class InternalServerErrorApiException extends ApiException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final String defaultMessage = "apiException.internalServerError";

    public InternalServerErrorApiException() {
        this(MessagesUtil.get(defaultMessage));
    }
    public InternalServerErrorApiException(String message) {
        super(message, HTTP_STATUS);
    }

}
