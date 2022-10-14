package hr.kraljic.web.module.core.permission;

public abstract class AbstractPermission {
    public AbstractPermission(PermissionInitializer permissionInitializer) {
        permissionInitializer.registerPermissions(registerPermissions());
    }

    public abstract String[] registerPermissions();
}
