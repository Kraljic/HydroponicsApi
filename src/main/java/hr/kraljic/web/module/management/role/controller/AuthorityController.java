package hr.kraljic.web.module.management.role.controller;

import hr.kraljic.web.module.management.role.RoleManagerPermission;
import hr.kraljic.web.module.management.role.service.AuthorityService;
import hr.kraljic.web.module.core.user.model.dto.AuthorityDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Kontroler za dohvacanje dozvola
 */
@RestController
@RequestMapping("/management/api/authority")
public class AuthorityController {
    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * Dohvaca sve dozvole.
     *
     * @return
     */
    @GetMapping
    @RoleManagerPermission.Read
    public List<AuthorityDto> getAllAuthorities() {
        return authorityService.getAllAuthorities();
    }
}
