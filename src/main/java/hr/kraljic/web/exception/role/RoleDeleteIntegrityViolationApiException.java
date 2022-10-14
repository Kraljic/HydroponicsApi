package hr.kraljic.web.exception.role;

import hr.kraljic.web.exception.generic.BadRequestApiException;
import hr.kraljic.web.util.MessagesUtil;

public class RoleDeleteIntegrityViolationApiException extends BadRequestApiException {
    private static final String defaultMessage = "apiException.integrityViolation.deleteRole";

    public RoleDeleteIntegrityViolationApiException(String roleName) {
        super(MessagesUtil.get(defaultMessage, roleName));
    }
}
