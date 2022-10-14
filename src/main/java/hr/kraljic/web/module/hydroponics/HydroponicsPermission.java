package hr.kraljic.web.module.hydroponics;

import hr.kraljic.web.module.core.permission.AbstractPermission;
import hr.kraljic.web.module.core.permission.PermissionInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Configuration
public class HydroponicsPermission extends AbstractPermission {
    public static final String PREFIX = "HYDROPONICS_";

    public static final String ACCESS = PREFIX + "ACCESS";
    public static final String READ = PREFIX + "READ";
    public static final String WRITE_DEVICE = PREFIX + "WRITE_DEVICE";
    public static final String WRITE_SENSOR = PREFIX + "WRITE_SENSOR";
    public static final String WRITE_SENSOR_VARIABLE = PREFIX + "WRITE_SENSOR_VARIABLE";
    public static final String WRITE_SENSOR_TYPE = PREFIX + "WRITE_SENSOR_TYPE";

    public HydroponicsPermission(PermissionInitializer permissionInitializer) {
        super(permissionInitializer);
    }

    @Override
    public String[] registerPermissions() {
        return new String[]{
                ACCESS,
                READ,
                WRITE_DEVICE,
                WRITE_SENSOR,
                WRITE_SENSOR_VARIABLE,
                WRITE_SENSOR_TYPE,
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
    @PreAuthorize("hasAuthority('" + WRITE_DEVICE + "')")
    public @interface WriteDevice {
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('" + WRITE_SENSOR + "')")
    public @interface WriteSensor {
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('" + WRITE_SENSOR_VARIABLE + "')")
    public @interface WriteSensorVariable {
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('" + WRITE_SENSOR_TYPE + "')")
    public @interface WriteSensorType {
    }

}
