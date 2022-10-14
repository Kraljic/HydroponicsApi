package hr.kraljic.web.module.core.authentication.model.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginCommand {
    @NotEmpty(message = "Username {valid.NotEmpty}")
    private String username;

    @NotEmpty(message = "Password {valid.NotEmpty}")
    private String password;
}
