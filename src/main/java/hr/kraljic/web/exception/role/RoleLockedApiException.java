package hr.kraljic.web.exception.role;

import hr.kraljic.web.exception.generic.ForbiddenApiException;
import hr.kraljic.web.util.MessagesUtil;
import org.springframework.http.HttpStatus;

public class RoleLockedApiException extends ForbiddenApiException {
    private static final String defaultMessage = "apiException.forbidden.writeProtectedRole";

    public RoleLockedApiException(String roleName) {
        super(MessagesUtil.get(defaultMessage, roleName));
    }
}
