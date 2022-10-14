package hr.kraljic.web.module.management.user;

import hr.kraljic.web.module.core.permission.AbstractPermission;
import hr.kraljic.web.module.core.permission.PermissionInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Configuration
public class UserManagerPermission extends AbstractPermission {
    private static final String PREFIX = "USERMANAGER_";
    public static final String ACCESS = PREFIX + "ACCESS";
    public static final String READ = PREFIX + "READ";
    public static final String WRITE = PREFIX + "WRITE";
    public static final String LOCK = PREFIX + "LOCK";
    public static final String ROLES = PREFIX + "ROLES";
    public static final String CREATE = PREFIX + "CREATE";

    public UserManagerPermission(PermissionInitializer permissionInitializer) {
        super(permissionInitializer);
    }

    @Override
    public String[] registerPermissions() {
        return new String[]{
                ACCESS,
                READ,
                WRITE,
                LOCK,
                ROLES,
                CREATE
        };
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('" + ACCESS + "')")
    public @interface Access {
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('" + READ + "')")
    public @interface Read {
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('" + WRITE + "')")
    public @interface Write {
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('" + LOCK + "')")
    public @interface Lock {
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('" + ROLES + "')")
    public @interface Roles {
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('" + CREATE + "')")
    public @interface Create {
    }
}
