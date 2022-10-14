package hr.kraljic.web.exception.role;

import hr.kraljic.web.exception.generic.NotFoundApiException;
import hr.kraljic.web.util.MessagesUtil;

public class RoleNotFoundApiException extends NotFoundApiException {
    private static final String defaultMessage = "apiException.notFound.role";
    private static final String defaultMessage_id = "apiException.notFound.roleId";

    public RoleNotFoundApiException(String roleName) {
        super(MessagesUtil.get(defaultMessage, roleName));
    }
    public RoleNotFoundApiException(Integer id) {
        super(MessagesUtil.get(defaultMessage_id, id));
    }
}
