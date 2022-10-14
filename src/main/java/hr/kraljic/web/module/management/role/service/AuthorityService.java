package hr.kraljic.web.module.management.role.service;

import hr.kraljic.web.module.core.user.model.dto.AuthorityDto;

import java.util.List;

public interface AuthorityService {
    /**
     * Dohvati sve dozvole
     * @return
     */
    List<AuthorityDto> getAllAuthorities();
}
