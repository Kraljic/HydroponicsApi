package hr.kraljic.web.module.management.user.model.command;

import hr.kraljic.web.util.validator.FieldMatchValidator;
import hr.kraljic.web.util.validator.PasswordPolicyValidator;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@FieldMatchValidator.FieldMatch(first = "newPassword", second = "newPasswordRepeat", message = "{valid.password.Match}")
public class PasswordManagementCommand {
    @NotEmpty(message = "New password {valid.NotEmpty}")
    @PasswordPolicyValidator.PasswordPolicy
    private String newPassword;

    @NotNull(message = "Repeat password {valid.NotNull}")
    private String newPasswordRepeat;
}
