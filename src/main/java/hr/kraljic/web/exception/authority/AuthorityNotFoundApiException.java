package hr.kraljic.web.exception.authority;

import hr.kraljic.web.exception.generic.NotFoundApiException;
import hr.kraljic.web.util.MessagesUtil;

public class AuthorityNotFoundApiException extends NotFoundApiException {
    private static final String defaultMessage = "apiException.notFound.authority";
    private static final String defaultMessage_id= "apiException.notFound.authorityId";

    public AuthorityNotFoundApiException(String authorityName) {
        super(MessagesUtil.get(defaultMessage, authorityName));
    }

    public AuthorityNotFoundApiException(Integer id) {
        super(MessagesUtil.get(defaultMessage_id, id));
    }
}
