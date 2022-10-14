package hr.kraljic.web.module.management.user.controller;

import hr.kraljic.web.module.management.user.model.command.RegisterUserCommand;
import hr.kraljic.web.module.management.user.model.command.UserRolesCommand;
import hr.kraljic.web.module.management.user.service.UserManagerService;
import hr.kraljic.web.module.core.user.model.dto.UserDto;
import hr.kraljic.web.module.management.user.model.command.PasswordManagementCommand;
import hr.kraljic.web.module.management.user.UserManagerPermission;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Kontroler za upravljanje korisnicima
 */
@RestController
@RequestMapping("/management/api/user")
public class UserManagerController {
    private final UserManagerService userManagerService;

    public UserManagerController(UserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    @GetMapping
    @UserManagerPermission.Read
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userManagerService.getAllUsers());
    }

    @GetMapping("/username/{username}")
    @UserManagerPermission.Read
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return userManagerService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/byRoleId/{roleId}")
    @UserManagerPermission.Read
    public ResponseEntity<List<UserDto>> getAllUsersByRoleId(@PathVariable Integer roleId)  {
        return ResponseEntity.ok(userManagerService.getAllUsersByRoleId(roleId));
    }

    @PostMapping("/create")
    @UserManagerPermission.Create
    public ResponseEntity<UserDto> createNewUser(@RequestBody @Valid RegisterUserCommand registerUserCommand) {
        return userManagerService.createNewUser(registerUserCommand)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @DeleteMapping("/delete/{username}")
    @UserManagerPermission.Write
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("username") String username) {
        userManagerService.deleteUser(username);
    }

    @GetMapping("/enable/{username}")
    @UserManagerPermission.Write
    public ResponseEntity<UserDto> enableUserAccount(@PathVariable("username") String username) {
        return userManagerService.enableUserAccount(username, true)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
    }

    @GetMapping("/disable/{username}")
    @UserManagerPermission.Write
    public ResponseEntity<UserDto> disableUserAccount(@PathVariable("username") String username) {
        return userManagerService.enableUserAccount(username, false)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
    }

    @PutMapping("/password/{username}")
    @UserManagerPermission.Write
    @ResponseStatus(HttpStatus.OK)
    public void changeUserPassword(@PathVariable("username") String username,
                                   @RequestBody PasswordManagementCommand passwordManagementCommand) {
        userManagerService.changeUserPassword(username, passwordManagementCommand);
    }

    @GetMapping("/lock/{username}")
    @UserManagerPermission.Lock
    @ResponseStatus(HttpStatus.OK)
    public void lockUser(@PathVariable("username") String username) {
        userManagerService.lockUser(username);
    }

    @GetMapping("/unlock/{username}")
    @UserManagerPermission.Lock
    @ResponseStatus(HttpStatus.OK)
    public void unlockUser(@PathVariable("username") String username) {
        userManagerService.unlockUser(username);
    }


    @PutMapping("/roles/{username}")
    @UserManagerPermission.Roles
    public ResponseEntity<UserDto> assignRoles(@PathVariable("username") String username,
                                               @RequestBody @Valid UserRolesCommand userRolesCommand) {
        return userManagerService.assignRoles(username, userRolesCommand)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}
