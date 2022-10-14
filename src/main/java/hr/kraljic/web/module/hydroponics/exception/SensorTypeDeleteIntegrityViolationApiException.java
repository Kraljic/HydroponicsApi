package hr.kraljic.web.module.hydroponics.exception;

import hr.kraljic.web.exception.generic.BadRequestApiException;
import hr.kraljic.web.util.MessagesUtil;

public class SensorTypeDeleteIntegrityViolationApiException extends BadRequestApiException {
    private static final String defaultMessage = "apiException.integrityViolation.deleteSensorType";

    public SensorTypeDeleteIntegrityViolationApiException(String sensorType) {
        super(MessagesUtil.get(defaultMessage, sensorType));
    }
}
