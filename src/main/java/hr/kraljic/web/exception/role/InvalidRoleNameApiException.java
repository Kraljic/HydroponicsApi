package hr.kraljic.web.exception.role;

import hr.kraljic.web.exception.generic.BadRequestApiException;
import hr.kraljic.web.util.MessagesUtil;

public class InvalidRoleNameApiException extends BadRequestApiException {
    private static final String defaultMessage = "apiException.badRequest.roleInvalidName";

    public InvalidRoleNameApiException(String roleName) {
        super(MessagesUtil.get(defaultMessage, roleName));
    }
}