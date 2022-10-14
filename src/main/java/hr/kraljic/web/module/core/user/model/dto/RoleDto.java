package hr.kraljic.web.module.core.user.model.dto;

import hr.kraljic.web.module.core.user.model.Role;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class RoleDto {
    private Integer id;
    private String role;
    private Boolean locked;
    private Set<AuthorityDto> authorities;

    public static RoleDto map(Role role) {
        RoleDto roleDto = new RoleDto();

        roleDto.setId(role.getId());
        roleDto.setRole(role.getRole());
        roleDto.setLocked(role.getLocked());

        roleDto.setAuthorities(
                role
                        .getAuthorities()
                        .stream()
                        .map(AuthorityDto::map)
                        .collect(Collectors.toSet())
        );

        return roleDto;
    }
}
