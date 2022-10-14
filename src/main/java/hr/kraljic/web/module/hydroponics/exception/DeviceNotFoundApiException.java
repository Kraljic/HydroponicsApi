package hr.kraljic.web.module.hydroponics.exception;

import hr.kraljic.web.exception.generic.NotFoundApiException;
import hr.kraljic.web.util.MessagesUtil;

public class DeviceNotFoundApiException extends NotFoundApiException {
    private static final String defaultMessage = "apiException.notFound.device";

    public DeviceNotFoundApiException(Integer id) {
        super(MessagesUtil.get(defaultMessage, id));
    }
}
