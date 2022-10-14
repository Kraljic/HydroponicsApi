package hr.kraljic.web.module.notification;

import hr.kraljic.web.module.core.permission.AbstractPermission;
import hr.kraljic.web.module.core.permission.PermissionInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Configuration
public class NotificationPermission extends AbstractPermission {
    public static final String PREFIX = "NOTIFICATION_";

    public static final String ACCESS = PREFIX + "ACCESS";

    public NotificationPermission(PermissionInitializer permissionInitializer) {
        super(permissionInitializer);
    }

    @Override
    public String[] registerPermissions() {
        return new String[]{
                ACCESS,
        };
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('" + ACCESS + "')")
    public @interface Access {
    }
}
