package hr.kraljic.web.module.core.user.controller;

import hr.kraljic.web.module.core.user.model.command.PasswordCommand;
import hr.kraljic.web.module.core.user.model.command.ProfileCommand;
import hr.kraljic.web.module.core.user.model.dto.ProfileDto;
import hr.kraljic.web.module.core.user.model.dto.UserDto;
import hr.kraljic.web.module.core.user.UserPermission;
import hr.kraljic.web.module.core.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Dohvati podatke o prijavljenom korisniku
     *
     * @return Podaci prijavljenog korisnika
     */
    @GetMapping("/me")
    @UserPermission.Read
    public ResponseEntity<UserDto> readUser() {
        Optional<UserDto> userDto = userService.me();

        return userDto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Dohvvati podatke profila prijavljenog korisnika
     *
     * @return Profil prijavljenog korisnikaProfil prijavljenog korisnika
     */
    @GetMapping("/profile")
    @UserPermission.Read
    public ResponseEntity<ProfileDto> readUserProfile() {
        Optional<ProfileDto> profileDto = userService.profile();

        return profileDto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Promjeni podatke profila prijavljenog korisnika
     *
     * @param profileCommand Novi podaci profile
     * @return Profil prijavljenog korisnika
     */
    @PutMapping("/profile")
    @UserPermission.Write
    public ResponseEntity<ProfileDto> updateProfile(@Valid @RequestBody ProfileCommand profileCommand) {
        Optional<ProfileDto> profileDto = userService.updateProfile(profileCommand);

        return profileDto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Promjeni lozinku prijavljenog korisnika
     *
     * @param passwordCommand Stara i nova lozinka
     */
    @PutMapping("/password")
    @UserPermission.Write
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@Valid @RequestBody PasswordCommand passwordCommand) {
        userService.updatePassword(passwordCommand);
    }
}
