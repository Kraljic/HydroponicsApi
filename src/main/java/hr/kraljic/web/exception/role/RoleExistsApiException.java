package hr.kraljic.web.exception.role;

import hr.kraljic.web.exception.ApiException;
import hr.kraljic.web.util.MessagesUtil;
import org.springframework.http.HttpStatus;

public class RoleExistsApiException extends ApiException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    private static final String defaultMessage = "apiException.conflict.roleExists";

    public RoleExistsApiException(String roleName) {
        super(MessagesUtil.get(defaultMessage, roleName), HTTP_STATUS);
    }
}
