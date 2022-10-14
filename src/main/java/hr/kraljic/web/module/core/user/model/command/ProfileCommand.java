package hr.kraljic.web.module.core.user.model.command;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ProfileCommand {
    @Size(max = 45, message = "First name {valid.Size.max}")
    private String firstName;

    @Size(max = 45, message = "Last name {valid.Size.max}")
    private String lastName;
}
