package hr.kraljic.web.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;


@Component
public class MessagesUtil {
    private static MessageSource messageSource;

    public MessagesUtil(MessageSource messageSource) {
        MessagesUtil.messageSource = messageSource;
    }

    public static String get(String code, Object... objects) {
        return messageSource.getMessage(code, objects, LocaleContextHolder.getLocale());
    }
}
