package hr.kraljic.web.module.management.user.model.command;

import hr.kraljic.web.util.validator.ValidEmailPattern;
import hr.kraljic.web.util.validator.PasswordPolicyValidator;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RegisterUserCommand {
    @NotEmpty(message = "Username {valid.NotEmpty}")
    private String username;

    @NotEmpty(message = "Password {valid.NotEmpty}")
    @PasswordPolicyValidator.PasswordPolicy
    private String password;

    @NotEmpty(message = "Email {valid.NotEmpty}")
    @Email(message = "{valid.email.invalidEmail}", regexp = ValidEmailPattern.PATTERN)
    private String email;

    @NotNull(message = "Roles {valid.NotNull}")
    private List<Integer> roles;
}
