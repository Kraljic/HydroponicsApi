package hr.kraljic.web.module.hydroponics.exception;

import hr.kraljic.web.exception.generic.BadRequestApiException;
import hr.kraljic.web.util.MessagesUtil;

public class DeviceDeleteIntegrityViolationApiException extends BadRequestApiException {
    private static final String defaultMessage = "apiException.integrityViolation.deleteDevice";

    public DeviceDeleteIntegrityViolationApiException(String deviceName) {
        super(MessagesUtil.get(defaultMessage, deviceName));
    }
}
