package hr.kraljic.web.module.core.permission;

public class PermissionNotAddedException extends RuntimeException {
    public PermissionNotAddedException(String permission) {
        super("Permission " + permission + " could not be added.");
    }
}
