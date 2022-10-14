package hr.kraljic.web.module.hydroponics.exception;

import hr.kraljic.web.exception.generic.NotFoundApiException;
import hr.kraljic.web.util.MessagesUtil;

public class SensorTypeNotFoundApiException extends NotFoundApiException {
    private static final String defaultMessage = "apiException.notFound.sensorType";

    public SensorTypeNotFoundApiException(Integer id) {
        super(MessagesUtil.get(defaultMessage, id));
    }
}
