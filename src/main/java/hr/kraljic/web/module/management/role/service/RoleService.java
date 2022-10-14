package hr.kraljic.web.module.management.role.service;

import hr.kraljic.web.module.management.role.model.command.RoleCommand;
import hr.kraljic.web.module.core.user.model.dto.RoleDto;

import java.util.List;
import java.util.Optional;

/**
 * Servis za upravljanje rolama i dozvolama rola
 */
public interface RoleService {
    /**
     * Dohvaca sve role
     * @return
     */
    List<RoleDto> getAllRoles();

    /**
     * Dohvaca rolu prema ID
     * @param roleId Id role koju se zeli dohvatiti
     * @return
     */
    Optional<RoleDto> getRoleById(Integer roleId);

    /**
     * Stvara novu rolu
     * @param roleCommand Informacije o novoj roli koja se zeli stvoriti
     * @return
     */
    Optional<RoleDto> creatRole(RoleCommand roleCommand);

    /**
     * Azurira informacije o roli koja ime pripadni ID
     * @param roleId ID role koju se zeli azurirati
     * @param roleCommand Informacije o roli
     * @return
     */
    Optional<RoleDto> updateRole(Integer roleId, RoleCommand roleCommand);

    /**
     * Brise rolu iz baze.
     * Ako se drugi entitei vezu na rolu, rola nece bti obrisana
     *
     * @param roleId ID role koja se zeli izbrisati
     */
    void deleteRole(Integer roleId);

    /**
     * Zakljucava rolu da se ne moze mijenjati.
     *
     * @param roleId Id role koju se zeli zakljucati
     */
    void lockRole(Integer roleId);

    /**
     * Otkljucava rolu da se moze mijenjati.
     *
     * @param roleId Id role koju se zeli otkljucati
     */
    void unlockRole(Integer roleId);

}
