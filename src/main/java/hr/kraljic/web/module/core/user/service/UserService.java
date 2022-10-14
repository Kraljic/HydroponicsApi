package hr.kraljic.web.module.core.user.service;

import hr.kraljic.web.exception.generic.UnauthorizedApiException;
import hr.kraljic.web.module.core.user.model.ApplicationUser;
import hr.kraljic.web.module.core.user.model.command.PasswordCommand;
import hr.kraljic.web.module.core.user.model.command.ProfileCommand;
import hr.kraljic.web.module.core.user.model.dto.ProfileDto;
import hr.kraljic.web.module.core.user.model.dto.UserDto;

import java.util.Optional;

public interface UserService {

    /**
     * Dohvati prijavljenog korisnika
     *
     * @return
     */
    Optional<UserDto> me();

    /**
     * Dohvati profil prijavljenog korisnika
     *
     * @return
     */
    Optional<ProfileDto> profile();

    /**
     * Azuriraj profil prijavljenog korisnika
     *
     * @param profileCommand
     * @return
     */
    Optional<ProfileDto> updateProfile(ProfileCommand profileCommand);

    /**
     * Promjeni lozinku prijavljenog korisnika
     *
     * @param passwordCommand
     */
    void updatePassword(PasswordCommand passwordCommand);

    /**
     * Dohvaca trenutno prijavljenog korisnika
     *
     * @return Prijavljenog korisnika
     * @throws UnauthorizedApiException
     */
    ApplicationUser getCurrentUser() throws UnauthorizedApiException;
}
