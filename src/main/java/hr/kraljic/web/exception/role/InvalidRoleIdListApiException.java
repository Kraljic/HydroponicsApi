package hr.kraljic.web.exception.role;

import hr.kraljic.web.exception.generic.BadRequestApiException;
import hr.kraljic.web.util.MessagesUtil;

public class InvalidRoleIdListApiException extends BadRequestApiException {
    private static final String defaultMessage = "apiException.badRequest.roleInvalidIdList";

    public InvalidRoleIdListApiException() {
        super(MessagesUtil.get(defaultMessage));
    }
}
