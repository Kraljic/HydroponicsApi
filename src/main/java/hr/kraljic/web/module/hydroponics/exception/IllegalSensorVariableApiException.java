package hr.kraljic.web.module.hydroponics.exception;

import hr.kraljic.web.exception.generic.BadRequestApiException;
import hr.kraljic.web.util.MessagesUtil;

public class IllegalSensorVariableApiException extends BadRequestApiException {
    private static final String defaultMessage = "apiException.badRequest.sensorVariable";

    public IllegalSensorVariableApiException() {
        super(MessagesUtil.get(defaultMessage));
    }
}
