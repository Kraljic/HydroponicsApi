package hr.kraljic.web.module.management.role.model.command;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RoleCommand {
    @NotBlank(message = "Role {valid.NotBlank}")
    private String role;

    @NotNull(message = "Authorities {valid.NotNull}")
    private List<Integer> authoritiesId;
}
