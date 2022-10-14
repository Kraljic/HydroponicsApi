package hr.kraljic.web.module.core.user.service;

import hr.kraljic.web.exception.generic.BadRequestApiException;
import hr.kraljic.web.exception.generic.UnauthorizedApiException;
import hr.kraljic.web.exception.user.InvalidPasswordApiException;
import hr.kraljic.web.module.core.user.model.ApplicationUser;
import hr.kraljic.web.module.core.user.model.Profile;
import hr.kraljic.web.module.core.user.model.command.PasswordCommand;
import hr.kraljic.web.module.core.user.model.command.ProfileCommand;
import hr.kraljic.web.module.core.user.model.dto.ProfileDto;
import hr.kraljic.web.module.core.user.model.dto.UserDto;
import hr.kraljic.web.module.core.user.repository.UserRepository;
import hr.kraljic.web.security.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Dohvati informacije trenutno prijavljenog korisnika
     *
     * @return Informacije trenutno prijavljenog korisnika
     */
    @Override
    public Optional<UserDto> me() {
        ApplicationUser currentUser = getCurrentUser();
        UserDto userDto = UserDto.map(currentUser);
        return Optional.of(userDto);
    }

    /**
     * Dohvati profil prijavljenog korisnika
     *
     * @return Profil prijavljenog korisnika
     */
    @Override
    public Optional<ProfileDto> profile() {
        ApplicationUser currentUser = getCurrentUser();
        ProfileDto profileDto = ProfileDto.map(currentUser.getProfile());
        return Optional.of(profileDto);
    }

    /**
     * Promjeni profil prijavljenog korisnika
     *
     * @param profileCommand Podaci profila korisnika
     * @return Profil korisnika
     */
    @Override
    public Optional<ProfileDto> updateProfile(ProfileCommand profileCommand) {
        ApplicationUser applicationUser = getCurrentUser();

        if (applicationUser.getProfile() == null) {
            applicationUser.setProfile(new Profile());
            applicationUser.getProfile().setUser(applicationUser);
        }

        applicationUser.getProfile().setFirstName(profileCommand.getFirstName());
        applicationUser.getProfile().setLastName(profileCommand.getLastName());

        applicationUser = userRepository.save(applicationUser);

        return Optional.of(ProfileDto.map(applicationUser.getProfile()));
    }

    /**
     * Promjeni lozinku trenutno prijavljenog korisnika
     *
     * @param passwordCommand Stara i nova lozinka korisnika
     */
    @Override
    public void updatePassword(PasswordCommand passwordCommand) {
        ApplicationUser applicationUser = getCurrentUser();

        // Provjeri da li je korisnik unio ispravnu trenutnu lozinku
        if (passwordEncoder.matches(passwordCommand.getOldPassword(), applicationUser.getPassword()) == false) {
            throw new InvalidPasswordApiException();
        }

        String newPasswordEncoded = passwordEncoder.encode(passwordCommand.getNewPassword());
        applicationUser.setPassword(newPasswordEncoded);

        userRepository.save(applicationUser);
    }

    /**
     * Dohvati prijavljenog korisnika.
     * Namjenjeno za pozive iz drugih servisa.
     *
     * @return Objekt prijavljenog korisnika
     * @throws UnauthorizedApiException
     */
    @Override
    public ApplicationUser getCurrentUser() throws UnauthorizedApiException {
        String currentUserUsername = SecurityUtils.getCurrentUserUsername()
                .orElseThrow(() -> new BadRequestApiException());

        ApplicationUser applicationUser = userRepository.findOneByUsername(currentUserUsername)
                .orElseThrow(() -> new BadRequestApiException());

        return applicationUser;
    }
}
