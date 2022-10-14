package hr.kraljic.web.module.management.user.model.command;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserRolesCommand {
    @NotNull(message = "Roles {valid.NotNull}")
    private List<Integer> roles;
}
