package hr.kraljic.web.module.core.user.model.command;

import hr.kraljic.web.util.validator.PasswordPolicyValidator.PasswordPolicy;
import hr.kraljic.web.util.validator.FieldMatchValidator.FieldMatch;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@FieldMatch(first = "newPassword", second = "newPasswordRepeat", message = "{valid.password.Match}")
public class PasswordCommand {
    @NotEmpty(message = "Old password {valid.NotEmpty}")
    private String oldPassword;

    @NotEmpty(message = "New password {valid.NotEmpty}")
    @PasswordPolicy
    private String newPassword;

    @NotNull(message = "Repeat password {valid.NotNull}")
    private String newPasswordRepeat;
}
