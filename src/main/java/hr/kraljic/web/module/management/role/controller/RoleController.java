package hr.kraljic.web.module.management.role.controller;

import hr.kraljic.web.module.management.role.model.command.RoleCommand;
import hr.kraljic.web.module.management.role.RoleManagerPermission;
import hr.kraljic.web.module.management.role.service.RoleService;
import hr.kraljic.web.module.core.user.model.dto.RoleDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Kontroler za upravljanje rolama i dozvolama rola
 */
@RestController
@RequestMapping("/management/api/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Dohvaca sve role.
     *
     * @return
     */
    @GetMapping
    @RoleManagerPermission.Read
    public List<RoleDto> getAllRoles() {
        return roleService.getAllRoles();
    }

    /**
     * Dohvaca rolu sa predanim ID-om
     *
     * @param roleId Id role koju zelimo dohvatiti
     * @return
     * @apiNote <b>API Exceptions:</b> <br/>
     * Role not found<br/>
     */
    @GetMapping("/{roleId}")
    @RoleManagerPermission.Read
    public ResponseEntity<RoleDto> getRoleById(@PathVariable("roleId") Integer roleId) {
        return roleService.getRoleById(roleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Dodaje novu rolu u sustav s odgovarajucim dozvolama
     *
     * @param roleCommand
     * @return
     * @apiNote <b>API Exceptions:</b> <br/>
     * Role with this name already exists<br/>
     * Authorities not found<br/>
     */
    @PostMapping
    @RoleManagerPermission.Write
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleCommand roleCommand) {
        return roleService.creatRole(roleCommand)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    /**
     * Mijenja postojecu rolu u sustavu
     *
     * @param roleId      Id role koju zelimo izmjeniti
     * @param roleCommand
     * @return
     * @apiNote <b>API Exceptions:</b> <br/>
     * Role not found<br/>
     * Role is write protected<br/>
     * Authorities not found<br/>
     */
    @PutMapping("/{roleId}")
    @RoleManagerPermission.Write
    public ResponseEntity<RoleDto> updateRole(@PathVariable("roleId") Integer roleId, @Valid @RequestBody RoleCommand roleCommand) {
        return roleService.updateRole(roleId, roleCommand)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Brise rolu iz sustava
     *
     * @param roleId Id role koju zelimo izbrisati
     * @apiNote <b>API Exceptions:</b> <br/>
     * Role not found<br/>
     * Role is write protected<br/>
     * Other entities are depended on role<br/>
     */
    @DeleteMapping("/{roleId}")
    @RoleManagerPermission.Write
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable("roleId") Integer roleId) {
        roleService.deleteRole(roleId);
    }


    /**
     * Zakljucava rolu da se ne moze mijenjati.
     *
     * @param roleId Id role koju se zeli zakljucati
     * @apiNote <b>API Exceptions:</b> <br/>
     * Role not found<br/>
     */
    @GetMapping("/lock/{roleId}")
    @RoleManagerPermission.Lock
    @ResponseStatus(HttpStatus.OK)
    public void lockRole(@PathVariable("roleId") Integer roleId) {
        roleService.lockRole(roleId);
    }

    /**
     * Otkljucava rolu da se moze mijenjati.
     *
     * @param roleId Id role koju se zeli otkljucati
     * @apiNote <b>API Exceptions:</b> <br/>
     * Role not found<br/>
     */
    @GetMapping("/unlock/{roleId}")
    @RoleManagerPermission.Lock
    @ResponseStatus(HttpStatus.OK)
    public void unlockRole(@PathVariable("roleId") Integer roleId) {
        roleService.unlockRole(roleId);
    }
}
