package hr.kraljic.web.module.management.user.service;

import hr.kraljic.web.BaseTestEnvironment;
import hr.kraljic.web.exception.role.InvalidRoleIdListApiException;
import hr.kraljic.web.exception.role.RoleNotFoundApiException;
import hr.kraljic.web.exception.user.EmailInUseApiException;
import hr.kraljic.web.exception.user.UserIsWriteProtectedApiException;
import hr.kraljic.web.exception.user.UserNotFoundApiException;
import hr.kraljic.web.exception.user.UsernameInUseApiException;
import hr.kraljic.web.module.management.user.model.command.PasswordManagementCommand;
import hr.kraljic.web.module.management.user.model.command.RegisterUserCommand;
import hr.kraljic.web.module.management.user.model.command.UserRolesCommand;
import hr.kraljic.web.module.core.user.model.ApplicationUser;
import hr.kraljic.web.module.core.user.model.dto.RoleDto;
import hr.kraljic.web.module.core.user.model.dto.UserDto;
import hr.kraljic.web.module.core.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserManagerServiceTest extends BaseTestEnvironment {
    private static final Integer ADMIN_USER_ID = 1; // ADMIN (locked)
    private static final Integer USER_USER_ID = 2; // USER

    private static final Integer ROLE_ADMIN_ID = 1; // ROLE_ADMIN (locked)
    private static final Integer ROLE_USER_ID = 2; // ROLE_USER
    private static final Integer ROLE_INDEPENDENT_ID = 3; // ROLE_INDEPENDENT


    /*
    - Get users by role id
    - Get users by role id (role not found)

    - Create new user
    - Create new user (username in use)
    - Create new user (email in use)
    - Create new user (roles not found)

    - Delete user
    - Delete user (user not found)
    - Delete user (user locked)

    - Change user active status
    - Change user active status (user not found)
    - Change user active status (user locked)

    - Change user password
    - Change user password (user not found)
    - Change user password (user locked)

    - Lock user
    - Lock user (user not found)
    - Unlock user
    - unlock user (user not found)

    - Assign roles to user
    - Assign roles to user (user not found)
    - Assign roles to user (user locked)
    - Assign roles to user (roles not found)
     */

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserManagerService userManagerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DirtiesContext
    @Transactional
    void getAllUsersByRoleId() {
        List<UserDto> allUsersByRoleId = userManagerService.getAllUsersByRoleId(ROLE_USER_ID);
        Optional<UserDto> first = allUsersByRoleId
                .stream()
                .filter(u -> u.getId().equals(ROLE_USER_ID))
                .findFirst();

        assertTrue(first.isPresent());
        assertEquals("user", first.get().getUsername());
    }

    @Test
    @DirtiesContext
    @Transactional
    void getAllUsersByRoleId_RoleNotFound() {
        assertThrows(RoleNotFoundApiException.class, () -> {
            userManagerService.getAllUsersByRoleId(-1);
        });
    }

    @Test
    @DirtiesContext
    @Transactional
    void createNewUser() {
        RegisterUserCommand userCommand =
                generateRegisterUserCommand("new_user", "new_user@test.hr", List.of(ROLE_ADMIN_ID));
        Optional<UserDto> newUser = userManagerService.createNewUser(userCommand);

        assertTrue(newUser.isPresent());
        UserDto userDto = newUser.get();
        assertEquals(userDto.getUsername(), userCommand.getUsername());
        assertEquals(userDto.getEmail(), userCommand.getEmail());
    }

    @Test
    @DirtiesContext
    @Transactional
    void createNewUser_UsernameInUse() {
        RegisterUserCommand userCommand =
                generateRegisterUserCommand("admin", "new_user@test.hr", List.of(ROLE_ADMIN_ID));

        assertThrows(UsernameInUseApiException.class, () -> {
            userManagerService.createNewUser(userCommand);
        });
    }

    @Test
    @DirtiesContext
    @Transactional
    void createNewUser_EmailInUse() {
        RegisterUserCommand userCommand =
                generateRegisterUserCommand("new_user", "admin@localhost", List.of(ROLE_ADMIN_ID));

        assertThrows(EmailInUseApiException.class, () -> {
            userManagerService.createNewUser(userCommand);
        });
    }

    @Test
    @DirtiesContext
    @Transactional
    void createNewUser_RoleNotFound() {
        RegisterUserCommand userCommand =
                generateRegisterUserCommand("new_user", "new_user@test.hr", List.of(-1));

        assertThrows(InvalidRoleIdListApiException.class, () -> {
            userManagerService.createNewUser(userCommand);
        });
    }

    @Test
    @DirtiesContext
    @Transactional
    void deleteUser() {
        Optional<ApplicationUser> userBefore = userRepository.findOneByUsername("user");
        assertTrue(userBefore.isPresent());

        userManagerService.deleteUser("user");

        Optional<ApplicationUser> userAfter = userRepository.findOneByUsername("user");
        assertFalse(userAfter.isPresent());
    }

    @Test
    @DirtiesContext
    @Transactional
    void deleteUser_UserNotFound() {
        assertThrows(UserNotFoundApiException.class, () -> {
            userManagerService.deleteUser("non_existing_user");
        });
    }

    @Test
    @DirtiesContext
    @Transactional
    void deleteUser_UserLocked() {
        assertThrows(UserIsWriteProtectedApiException.class, () -> {
            userManagerService.deleteUser("admin");
        });
    }

    @Test
    @DirtiesContext
    @Transactional
    void changeUserActiveStatus() {
        final boolean accountEnable = false;

        Optional<UserDto> userDto = userManagerService.enableUserAccount("user", accountEnable);

        assertTrue(userDto.isPresent());
        assertEquals(accountEnable, userDto.get().isActive());
    }

    @Test
    @DirtiesContext
    @Transactional
    void changeUserActiveStatus_UserNotFound() {
        final boolean accountEnable = false;

        assertThrows(UserNotFoundApiException.class, () -> {
            userManagerService.enableUserAccount("non_existing_user", accountEnable);
        });
    }

    @Test
    @DirtiesContext
    @Transactional
    void changeUserActiveStatus_UserWriteProtected() {
        final boolean accountEnable = false;

        assertThrows(UserIsWriteProtectedApiException.class, () -> {
            userManagerService.enableUserAccount("admin", accountEnable);
        });
    }

    @Test
    @DirtiesContext
    @Transactional
    void changeUserPassword() {
        final String newPassword = "test123";
        PasswordManagementCommand newPasswordCmd = new PasswordManagementCommand();
        newPasswordCmd.setNewPassword(newPassword);
        newPasswordCmd.setNewPasswordRepeat(newPassword);

        userManagerService.changeUserPassword("user", newPasswordCmd);

        Optional<ApplicationUser> user = userRepository.findOneByUsername("user");

        assertTrue(user.isPresent());
        assertTrue(passwordEncoder.matches(newPassword, user.get().getPassword()));
    }

    @Test
    @DirtiesContext
    @Transactional
    void changeUserPassword_UserNotFound() {
        PasswordManagementCommand newPassword = new PasswordManagementCommand();
        newPassword.setNewPassword("test123");
        newPassword.setNewPasswordRepeat("test123");

        assertThrows(UserNotFoundApiException.class, () -> {
            userManagerService.changeUserPassword("non_existing_user", newPassword);
        });
    }

    @Test
    @DirtiesContext
    @Transactional
    void changeUserPassword_UserWriteProtected() {
        PasswordManagementCommand newPassword = new PasswordManagementCommand();
        newPassword.setNewPassword("test123");
        newPassword.setNewPasswordRepeat("test123");

        assertThrows(UserIsWriteProtectedApiException.class, () -> {
            userManagerService.changeUserPassword("admin", newPassword);
        });
    }

    @Test
    @DirtiesContext
    @Transactional
    void lockUser() {
        userManagerService.lockUser("user");
    }

    @Test
    @DirtiesContext
    @Transactional
    void lockUser_UserNotFound() {
        assertThrows(UserNotFoundApiException.class, () -> {
            userManagerService.lockUser("non_existing_user");
        });
    }

    @Test
    @DirtiesContext
    @Transactional
    void unlockUser() {
        userManagerService.unlockUser("user");
    }

    @Test
    @DirtiesContext
    @Transactional
    void unlockUser_UserNotFound() {
        assertThrows(UserNotFoundApiException.class, () -> {
            userManagerService.unlockUser("non_existing_user");
        });
    }

    @Test
    @DirtiesContext
    @Transactional
    void assignRoles() {
        UserRolesCommand userRolesCommand = new UserRolesCommand();
        userRolesCommand.setRoles(List.of(ROLE_ADMIN_ID, ROLE_INDEPENDENT_ID));

        Optional<UserDto> userBefore = userManagerService.getUserByUsername("user");
        assertTrue(userBefore.isPresent());
        Set<RoleDto> rolesBefore = userBefore.get().getRoles();

        userManagerService.assignRoles("user", userRolesCommand);

        Optional<UserDto> userAfter = userManagerService.getUserByUsername("user");
        assertTrue(userAfter.isPresent());
        Set<RoleDto> rolesAfter = userAfter.get().getRoles();

        assertNotEquals(rolesBefore, rolesAfter);
    }

    @Test
    @DirtiesContext
    @Transactional
    void assignRoles_UserNotFound() {
        UserRolesCommand userRolesCommand = new UserRolesCommand();
        userRolesCommand.setRoles(List.of(ROLE_ADMIN_ID));

        assertThrows(UserNotFoundApiException.class, () -> {
            userManagerService.assignRoles("non_existing_user", userRolesCommand);
        });
    }

    @Test
    @DirtiesContext
    @Transactional
    void assignRoles_UserLocked() {
        UserRolesCommand userRolesCommand = new UserRolesCommand();
        userRolesCommand.setRoles(List.of(ROLE_USER_ID));

        assertThrows(UserIsWriteProtectedApiException.class, () -> {
            userManagerService.assignRoles("admin", userRolesCommand);
        });
    }

    @Test
    @DirtiesContext
    @Transactional
    void assignRoles_InvalidRoleIdList() {
        UserRolesCommand userRolesCommand = new UserRolesCommand();
        userRolesCommand.setRoles(List.of(ROLE_USER_ID, -1));

        assertThrows(InvalidRoleIdListApiException.class, () -> {
            userManagerService.assignRoles("user", userRolesCommand);
        });
    }

    private RegisterUserCommand generateRegisterUserCommand(String username, String email, List<Integer> rolesId) {
        RegisterUserCommand userCommand = new RegisterUserCommand();
        userCommand.setUsername(username);
        userCommand.setEmail(email);
        userCommand.setPassword("admin");
        userCommand.setRoles(rolesId);

        return userCommand;
    }
}