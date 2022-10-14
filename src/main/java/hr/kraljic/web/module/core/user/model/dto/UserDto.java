package hr.kraljic.web.module.core.user.model.dto;

import hr.kraljic.web.module.core.user.model.ApplicationUser;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private boolean active;
    private boolean writeProtected;
    private Set<RoleDto> roles;
    private Set<String> authorities;

    /**
     * Mapira application user objekt na user dto objekt
     *
     * @param applicationUser Objekt korisnika
     * @return mapirani user dto objekt
     */
    public static UserDto map(ApplicationUser applicationUser) {
        if (applicationUser == null)
            return null;

        UserDto userDto = new UserDto();
        userDto.setId(applicationUser.getId());
        userDto.setUsername(applicationUser.getUsername());
        userDto.setEmail(applicationUser.getEmail());

        userDto.setActive(applicationUser.isActive());
        userDto.setWriteProtected(applicationUser.isWriteProtected());

        userDto.setRoles(
                applicationUser
                        .getRoles()
                        .stream()
                        .map(RoleDto::map)
                        .collect(Collectors.toSet())
        );

        userDto.setAuthorities(
                applicationUser
                        .getAuthorities()
                        .stream()
                        .map(a -> a.getAuthority())
                        .collect(Collectors.toSet()));

        return userDto;
    }
}
