package hr.kraljic.web.module.management.user.service;

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
import hr.kraljic.web.module.core.user.model.Role;
import hr.kraljic.web.module.core.user.model.dto.UserDto;
import hr.kraljic.web.module.core.user.repository.RoleRepository;
import hr.kraljic.web.module.core.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserManagerServiceImpl implements UserManagerService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserManagerServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllUsersByRoleId(Integer roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isEmpty()) {
            throw new RoleNotFoundApiException(roleId);
        }

        return userRepository.findByRole(roleId)
                .stream()
                .map(UserDto::map)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUserByUsername(String username) {
        UserDto user = userRepository
                .findOneByUsername(username)
                .map(UserDto::map)
                .orElseThrow(() -> new UserNotFoundApiException(username));
        return Optional.of(user);
    }

    @Override
    public Optional<UserDto> createNewUser(RegisterUserCommand registerUserCommand) {
        boolean usernameInUse = userRepository.existsByUsername(registerUserCommand.getUsername());
        if (usernameInUse) {
            throw new UsernameInUseApiException(registerUserCommand.getUsername());
        }

        boolean emailInUse = userRepository.existsByEmail(registerUserCommand.getEmail());
        if (emailInUse) {
            throw new EmailInUseApiException(registerUserCommand.getEmail());
        }

        ApplicationUser newUser = mapToApplicationUser(registerUserCommand);

        newUser = userRepository.save(newUser);

        UserDto userDto = UserDto.map(newUser);
        return Optional.of(userDto);
    }

    @Override
    public void deleteUser(String username) {
        ApplicationUser applicationUser = findByUsername(username);

        if (applicationUser.isWriteProtected()) {
            throw new UserIsWriteProtectedApiException(username);
        }

        userRepository.deleteById(applicationUser.getId());
    }

    @Override
    public Optional<UserDto> enableUserAccount(String username, boolean enabled) {
        ApplicationUser applicationUser = findByUsername(username);

        if (applicationUser.isWriteProtected()) {
            throw new UserIsWriteProtectedApiException(username);
        }

        applicationUser.setActive(enabled);
        applicationUser = userRepository.save(applicationUser);

        UserDto userDto = UserDto.map(applicationUser);

        return Optional.of(userDto);
    }

    @Override
    public void changeUserPassword(String username, PasswordManagementCommand passwordManagementCommand) {
        ApplicationUser applicationUser = findByUsername(username);

        if (applicationUser.isWriteProtected()) {
            throw new UserIsWriteProtectedApiException(username);
        }

        String passwordEncoded = passwordEncoder.encode(passwordManagementCommand.getNewPassword());
        applicationUser.setPassword(passwordEncoded);

        userRepository.save(applicationUser);
    }

    @Override
    public void lockUser(String username) {
        ApplicationUser applicationUser = findByUsername(username);
        applicationUser.setWriteProtected(true);
        userRepository.save(applicationUser);
    }

    @Override
    public void unlockUser(String username) {
        ApplicationUser applicationUser = findByUsername(username);
        applicationUser.setWriteProtected(false);
        userRepository.save(applicationUser);
    }

    @Override
    public Optional<UserDto> assignRoles(String username, UserRolesCommand userRolesCommand) {
        ApplicationUser applicationUser = findByUsername(username);

        if (applicationUser.isWriteProtected()) {
            throw new UserIsWriteProtectedApiException(username);
        }

        Set<Role> roles = mapRolesIdToRoles(userRolesCommand.getRoles());

        applicationUser.setRoles(roles);
        applicationUser = userRepository.save(applicationUser);

        UserDto userDto = UserDto.map(applicationUser);

        return Optional.of(userDto);
    }

    private ApplicationUser findByUsername(String username) {
        return userRepository.findOneByUsername(username)
                .orElseThrow(() -> new UserNotFoundApiException(username));

    }

    private ApplicationUser mapToApplicationUser(RegisterUserCommand registerUserCommand) {
        ApplicationUser newUser = new ApplicationUser();
        newUser.setUsername(registerUserCommand.getUsername());
        newUser.setEmail(registerUserCommand.getEmail());
        newUser.setActive(true);
        newUser.setWriteProtected(false);

        String encodedPassword = passwordEncoder.encode(registerUserCommand.getPassword());
        newUser.setPassword(encodedPassword);

        Set<Role> roles = mapRolesIdToRoles(registerUserCommand.getRoles());
        newUser.setRoles(roles);

        return newUser;
    }

    private Set<Role> mapRolesIdToRoles(List<Integer> rolesId) {
        List<Role> mappedRoles = roleRepository.findAllById(rolesId);

        // Provjeri da li su sve role pronadene
        if (mappedRoles.size() < rolesId.size()) {
            throw new InvalidRoleIdListApiException();
        }

        return mappedRoles.stream().collect(Collectors.toSet());
    }
}
