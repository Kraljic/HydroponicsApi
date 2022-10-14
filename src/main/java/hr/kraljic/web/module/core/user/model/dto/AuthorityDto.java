package hr.kraljic.web.module.core.user.model.dto;

import hr.kraljic.web.module.core.user.model.Authority;
import lombok.Data;

@Data
public class AuthorityDto {
    private Integer id;
    private String authority;

    public static AuthorityDto map(Authority authority) {
        AuthorityDto authorityDto = new AuthorityDto();
        authorityDto.setId(authority.getId());
        authorityDto.setAuthority(authority.getAuthority());

        return authorityDto;
    }
}
