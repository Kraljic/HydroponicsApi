package hr.kraljic.web.module.management.user.service;


import hr.kraljic.web.module.management.user.model.command.RegisterUserCommand;
import hr.kraljic.web.module.management.user.model.command.UserRolesCommand;
import hr.kraljic.web.module.core.user.model.dto.UserDto;
import hr.kraljic.web.module.management.user.model.command.PasswordManagementCommand;

import java.util.List;
import java.util.Optional;

public interface UserManagerService {
    /**
     * Dohvaca sve korisnike
     *
     * @return
     */
    List<UserDto> getAllUsers();

    /**
     * Dohvaca sve korisnike koji pripadaju roli
     *
     * @param roleId ID role cije korisnike zelimo dohvatiti
     * @return
     */
    List<UserDto> getAllUsersByRoleId(Integer roleId);

    /**
     * Dohvati korisnika po korisnickom imenu
     *
     * @param username Korisnicko ime
     * @return
     */
    Optional<UserDto> getUserByUsername(String username);

    /**
     * Stvori novi korisnicki racun
     *
     * @param registerUserCommand Informacije o novom korisnickom racunu
     * @return Novi korisnicki racun
     */
    Optional<UserDto> createNewUser(RegisterUserCommand registerUserCommand);

    /**
     * Obrisi postojeceg korisnika
     *
     * @param username Korisnicko ime korisnika kojeg se zeli obrisati
     */
    void deleteUser(String username);

    /**
     * Aktiviraj ili deaktiviraj korisnicki racun
     *
     * @param username Korisnicko ime racuna koji se zeli deaktivirati (aktivirati)
     * @param enabled  {@code false} ako se korisnicki racun zeli deaktivirati,
     *                 {@code true} ako se korisnicki racun zeli aktivirati
     */
    Optional<UserDto> enableUserAccount(String username, boolean enabled);

    /**
     * Promjeni lozinku korisnika
     *
     * @param username                  Korisnicko ime korisnika kojem se zeli promjeniti lozinka
     * @param passwordManagementCommand Nova lozinka
     */
    void changeUserPassword(String username, PasswordManagementCommand passwordManagementCommand);

    /**
     * Zakljucaj korisnicki racun da se ne moze mijenjati od drugih korisnika
     *
     * @param username Korisnicko ime korisnika kojeg se zeli zakljucati
     */
    void lockUser(String username);

    /**
     * Otkljucaj korisnicki racun da se moze mijenjati od drugih korisnika
     *
     * @param username Korisnicko ime korisnika kojeg se zeli otkljucati
     */
    void unlockUser(String username);

    /**
     * Dodjeljuje navedene role korisniku (sve ostale se uklanjaju)
     *
     * @param username Korisnicko ime korisnika kojem se zele dodjeliti role
     * @param userRolesCommand  Lista ID rola koje se dodjeljuju korisniku
     */
    Optional<UserDto> assignRoles(String username, UserRolesCommand userRolesCommand);
}
