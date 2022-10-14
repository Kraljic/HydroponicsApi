package hr.kraljic.web.module.core.user.model.dto;

import hr.kraljic.web.module.core.user.model.ApplicationUser;
import lombok.Data;

@Data
public class SimpleUserDto {
    private Integer id;
    private String username;
    private Boolean active;

    public static SimpleUserDto map(ApplicationUser user) {
        SimpleUserDto dto = new SimpleUserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setActive(user.isActive());

        return dto;
    }
}
