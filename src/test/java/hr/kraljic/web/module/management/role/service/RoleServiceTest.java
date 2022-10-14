package hr.kraljic.web.module.management.role.service;

import hr.kraljic.web.BaseTestEnvironment;
import hr.kraljic.web.exception.authority.InvalidAuthorityIdListApiException;
import hr.kraljic.web.exception.role.*;
import hr.kraljic.web.module.management.role.model.command.RoleCommand;
import hr.kraljic.web.module.core.user.model.dto.RoleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoleServiceTest extends BaseTestEnvironment {
    /*
    - Create new role
    - Create new role (role exists)
    - Create new role (authorities not found)
    - Create new role (naming convention)

    - Update role
    - Update role (role not exists)
    - Update role (role is locked)
    - Update role (rename -> role exists)
    - Update role (authorities not found)
    - Update role (naming convention)

    - Delete role
    - Delete role (role not exists)
    - Delete role (role is locked)
    - Delete role (role is depended on other entities)

    - Lock role
    - Lock role (role not exists)

    - Unlock role
    - Unlock role (role not exists)
     */

    private static final Integer ADMIN_ROLE_ID = 1; // ROLE_ADMIN (locked)
    private static final Integer USER_ROLE_ID = 2; // ROLE_USER
    private static final Integer INDEPENDENT_ROLE_ID = 3; // ROLE_INDEPENDENT (no connection to user or authorities)

    @Autowired
    private RoleService roleService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DirtiesContext
    void createNewRole_Test() {
        RoleCommand newRole = new RoleCommand();
        newRole.setRole("ROLE_NEW");
        newRole.setAuthoritiesId(List.of(1, 2));

        Optional<RoleDto> roleDto = roleService.creatRole(newRole);

        assertTrue(roleDto.isPresent());

        assertEquals(newRole.getRole(), roleDto.get().getRole());
        assertEquals(2, roleDto.get().getAuthorities().size());
        assertFalse(roleDto.get().getLocked());
    }

    @Test
    @DirtiesContext
    void createNewRole_RoleExistsTest() {
        RoleCommand newRole = new RoleCommand();
        newRole.setRole("ROLE_ADMIN");
        newRole.setAuthoritiesId(List.of(1, 2));

        assertThrows(RoleExistsApiException.class, () -> {
            roleService.creatRole(newRole);
        });
    }

    @Test
    @DirtiesContext
    void createNewRole_AuthoritiesNotFoundTest() {
        RoleCommand newRole = new RoleCommand();
        newRole.setRole("ROLE_NEW");
        newRole.setAuthoritiesId(List.of(-1, 2));

        assertThrows(InvalidAuthorityIdListApiException.class, () -> {
            roleService.creatRole(newRole);
        });
    }

    @Test
    @DirtiesContext
    void createNewRole_NamingConventionTest() {
        RoleCommand newRole = new RoleCommand();
        newRole.setRole("NEW");
        newRole.setAuthoritiesId(List.of(1, 2));

        Optional<RoleDto> roleDto = roleService.creatRole(newRole);
        assertTrue(roleDto.isPresent());
        assertEquals("ROLE_NEW", roleDto.get().getRole());
    }


    @Test
    @DirtiesContext
    void updateRole_Test() {
        RoleCommand updatedRole = new RoleCommand();
        updatedRole.setRole("ROLE_UPDATED");
        updatedRole.setAuthoritiesId(List.of(1, 2));

        Optional<RoleDto> roleDto = roleService.updateRole(USER_ROLE_ID, updatedRole);

        assertTrue(roleDto.isPresent());

        assertEquals(updatedRole.getRole(), roleDto.get().getRole());
        assertEquals(2, roleDto.get().getAuthorities().size());
        assertFalse(roleDto.get().getLocked());
    }

    @Test
    @DirtiesContext
    void updateRole_RoleNotFoundTest() {
        RoleCommand updatedRole = new RoleCommand();
        updatedRole.setRole("ROLE_UPDATED");
        updatedRole.setAuthoritiesId(List.of(1, 2));

        assertThrows(RoleNotFoundApiException.class, () -> {
            roleService.updateRole(-1, updatedRole);
        });
    }

    @Test
    @Transactional
    @DirtiesContext
    void updateRole_AuthoritiesNotFoundTest() {
        RoleCommand updatedRole = new RoleCommand();
        updatedRole.setRole("ROLE_UPDATED");
        updatedRole.setAuthoritiesId(List.of(-1, 2));

        assertThrows(InvalidAuthorityIdListApiException.class, () -> {
            roleService.updateRole(USER_ROLE_ID, updatedRole);
        });
    }

    @Test
    @DirtiesContext
    void updateRole_RoleLockedTest() {
        RoleCommand updatedRole = new RoleCommand();
        updatedRole.setRole("ROLE_UPDATED");
        updatedRole.setAuthoritiesId(List.of(1, 2));

        assertThrows(RoleLockedApiException.class, () -> {
            roleService.updateRole(ADMIN_ROLE_ID, updatedRole);
        });
    }

    @Test
    @DirtiesContext
    void updateRole_RenameRoleExistsTest() {
        RoleCommand newRole = new RoleCommand();
        newRole.setRole("ROLE_ADMIN"); // Try to rename ROLE_USER to ROLE_ADMIN
        newRole.setAuthoritiesId(List.of(1, 2));

        assertThrows(RoleExistsApiException.class, () -> {
            roleService.updateRole(USER_ROLE_ID, newRole);
        });
    }

    @Test
    @DirtiesContext
    void updateRole_NamingConventionTest() {
        RoleCommand newRole = new RoleCommand();
        newRole.setRole("USER");
        newRole.setAuthoritiesId(List.of(1, 2));

        Optional<RoleDto> roleDto = roleService.updateRole(USER_ROLE_ID, newRole);
        assertTrue(roleDto.isPresent());
        assertEquals("ROLE_USER", roleDto.get().getRole());
    }

    @Test
    @DirtiesContext
    void deleteRole_Test() {
        Optional<RoleDto> roleBefore = roleService.getRoleById(INDEPENDENT_ROLE_ID);
        assertTrue(roleBefore.isPresent());

        roleService.deleteRole(INDEPENDENT_ROLE_ID);

        Optional<RoleDto> roleAfter = roleService.getRoleById(INDEPENDENT_ROLE_ID);
        assertTrue(roleAfter.isEmpty());
    }

    @Test
    @DirtiesContext
    void deleteRole_RoleNotFoundTest() {
        assertThrows(RoleNotFoundApiException.class, () -> {
            roleService.deleteRole(-1);
        });
    }

    @Test
    @DirtiesContext
    void deleteRole_RoleLockedTest() {
        assertThrows(RoleLockedApiException.class, () -> {
            roleService.deleteRole(ADMIN_ROLE_ID);
        });

        Optional<RoleDto> roleAfter = roleService.getRoleById(ADMIN_ROLE_ID);
        assertTrue(roleAfter.isPresent());
    }

    @Test
    @DirtiesContext
    void deleteRole_IntegrityViolationTest() {
        Optional<RoleDto> roleBefore = roleService.getRoleById(USER_ROLE_ID);
        assertTrue(roleBefore.isPresent());

        assertThrows(RoleDeleteIntegrityViolationApiException.class, () -> {
            roleService.deleteRole(USER_ROLE_ID);
        });

        Optional<RoleDto> roleAfter = roleService.getRoleById(USER_ROLE_ID);
        assertTrue(roleAfter.isPresent());
    }


    @Test
    @DirtiesContext
    void lockRole_Test() {
        Optional<RoleDto> roleBefore = roleService.getRoleById(USER_ROLE_ID);
        assertTrue(roleBefore.isPresent());
        assertFalse(roleBefore.get().getLocked());

        roleService.lockRole(USER_ROLE_ID);

        Optional<RoleDto> roleAfter = roleService.getRoleById(USER_ROLE_ID);
        assertTrue(roleAfter.isPresent());
        assertTrue(roleAfter.get().getLocked());
    }

    @Test
    @DirtiesContext
    void lockRole_RoleNotFoundTest() {
        assertThrows(RoleNotFoundApiException.class, () -> {
            roleService.unlockRole(-1);
        });
    }

    @Test
    @DirtiesContext
    void unlockRole_Test() {
        Optional<RoleDto> roleBefore = roleService.getRoleById(ADMIN_ROLE_ID);
        assertTrue(roleBefore.isPresent());
        assertTrue(roleBefore.get().getLocked());

        roleService.unlockRole(ADMIN_ROLE_ID);

        Optional<RoleDto> roleAfter = roleService.getRoleById(ADMIN_ROLE_ID);
        assertTrue(roleAfter.isPresent());
        assertFalse(roleAfter.get().getLocked());
    }

    @Test
    @DirtiesContext
    void unlockRole_RoleNotFoundTest() {
        assertThrows(RoleNotFoundApiException.class, () -> {
            roleService.unlockRole(-1);
        });
    }
}