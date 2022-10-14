package hr.kraljic.web.module.core.permission.event;

import hr.kraljic.web.module.core.user.model.Authority;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AuthorityInitDoneEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public AuthorityInitDoneEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishAuthorityInitDoneEvent(final Set<Authority> authorities) {
        AuthorityInitDoneEvent authorityInitDoneEvent = new AuthorityInitDoneEvent(this, authorities);
        applicationEventPublisher.publishEvent(authorityInitDoneEvent);
    }
}
