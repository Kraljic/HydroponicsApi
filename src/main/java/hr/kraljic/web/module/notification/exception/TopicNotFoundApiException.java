package hr.kraljic.web.module.notification.exception;

import hr.kraljic.web.exception.generic.NotFoundApiException;
import hr.kraljic.web.util.MessagesUtil;

public class TopicNotFoundApiException extends NotFoundApiException {
    private static final String defaultMessage = "apiException.notFound.topicId";

    public TopicNotFoundApiException(Integer id) {
        super(MessagesUtil.get(defaultMessage, id));
    }
}
