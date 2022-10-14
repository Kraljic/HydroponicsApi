package hr.kraljic.web.module.core.permission.event;

import hr.kraljic.web.module.core.user.model.Authority;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Set;

@Getter
public class AuthorityInitDoneEvent extends ApplicationEvent {
    private Set<Authority> authorities;

    public AuthorityInitDoneEvent(Object source, Set<Authority> authorities) {
        super(source);
        this.authorities = authorities;
    }
}
