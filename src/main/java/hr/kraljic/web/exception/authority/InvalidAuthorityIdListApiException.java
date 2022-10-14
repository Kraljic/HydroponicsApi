package hr.kraljic.web.exception.authority;

import hr.kraljic.web.exception.generic.BadRequestApiException;
import hr.kraljic.web.util.MessagesUtil;

public class InvalidAuthorityIdListApiException extends BadRequestApiException {
    private static final String defaultMessage = "apiException.badRequest.authorityInvalidIdList";

    public InvalidAuthorityIdListApiException() {
        super(MessagesUtil.get(defaultMessage));
    }

}
