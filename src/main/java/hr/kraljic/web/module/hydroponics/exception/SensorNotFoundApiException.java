package hr.kraljic.web.module.hydroponics.exception;

import hr.kraljic.web.exception.generic.NotFoundApiException;
import hr.kraljic.web.util.MessagesUtil;

public class SensorNotFoundApiException extends NotFoundApiException {
    private static final String defaultMessage = "apiException.notFound.sensor";

    public SensorNotFoundApiException(Integer id) {
        super(MessagesUtil.get(defaultMessage, id));
    }
}
