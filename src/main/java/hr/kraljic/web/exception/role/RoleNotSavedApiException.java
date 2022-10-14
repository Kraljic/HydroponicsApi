package hr.kraljic.web.exception.role;

import hr.kraljic.web.exception.generic.InternalServerErrorApiException;
import hr.kraljic.web.util.MessagesUtil;

public class RoleNotSavedApiException extends InternalServerErrorApiException {
    private static final String defaultMessage = "apiException.internalServerError.roleNotSaved";

    public RoleNotSavedApiException(String roleName) {
        super(MessagesUtil.get(defaultMessage, roleName));
    }
}
