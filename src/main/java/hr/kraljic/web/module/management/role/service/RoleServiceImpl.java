package hr.kraljic.web.module.management.role.service;

import hr.kraljic.web.exception.authority.InvalidAuthorityIdListApiException;
import hr.kraljic.web.exception.role.*;
import hr.kraljic.web.module.management.role.model.command.RoleCommand;
import hr.kraljic.web.module.core.user.model.Authority;
import hr.kraljic.web.module.core.user.model.Role;
import hr.kraljic.web.module.core.user.model.dto.RoleDto;
import hr.kraljic.web.module.core.user.repository.AuthorityRepository;
import hr.kraljic.web.module.core.user.repository.RoleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String ROLE_VALIDATION_REGEX = "^ROLE_[A-Z_]+$";

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    public RoleServiceImpl(RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream().map(RoleDto::map).collect(Collectors.toList());
    }

    @Override
    public Optional<RoleDto> getRoleById(Integer roleId) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);

        if (roleOptional.isEmpty()) {
            return Optional.empty();
        }

        RoleDto roleDto = RoleDto.map(roleOptional.get());
        return Optional.of(roleDto);
    }

    @Override
    public Optional<RoleDto> creatRole(RoleCommand roleCommand) {
        roleCommand.setRole(roleNameToConvention(roleCommand.getRole()));

        boolean roleExists = roleRepository.existsByRole(roleCommand.getRole());
        if (roleExists) {
            throw new RoleExistsApiException(roleCommand.getRole());
        }

        Set<Authority> authorities = mapAuthoritiesIdToAuthorities(roleCommand.getAuthoritiesId());

        Role role = new Role();
        role.setRole(roleCommand.getRole());
        role.setAuthorities(authorities);
        role.setLocked(false);

        Role savedRole = roleRepository.save(role);
        if (savedRole == null) {
            throw new RoleNotSavedApiException(role.getRole());
        }
        RoleDto roleDto = RoleDto.map(savedRole);
        return Optional.of(roleDto);
    }

    @Override
    public Optional<RoleDto> updateRole(Integer roleId, RoleCommand roleCommand) {
        roleCommand.setRole(roleNameToConvention(roleCommand.getRole()));

        Role role = findRoleById(roleId);

        if (role.getLocked() == true) {
            throw new RoleLockedApiException(role.getRole());
        }

        // Provjeri da li korisnik pokusava promjeniti naziv role
        // u naziv neke druge role koja vec postoji u bazi
        boolean roleExists = roleRepository.existsByRole(roleCommand.getRole());
        if (roleExists && role.getRole().equals(roleCommand.getRole()) == false) {
            throw new RoleExistsApiException(roleCommand.getRole());
        }

        Set<Authority> authorities = mapAuthoritiesIdToAuthorities(roleCommand.getAuthoritiesId());

        role.setRole(roleCommand.getRole());
        role.setAuthorities(authorities);

        Role savedRole = roleRepository.save(role);
        if (savedRole == null) {
            throw new RoleNotSavedApiException(role.getRole());
        }

        RoleDto roleDto = RoleDto.map(savedRole);
        return Optional.of(roleDto);
    }

    @Override
    public void deleteRole(Integer roleId) {
        Role role = findRoleById(roleId);

        if (role.getLocked() == true) {
            throw new RoleLockedApiException(role.getRole());
        }

        try {
            roleRepository.deleteById(roleId);
        } catch (DataIntegrityViolationException e) {
            throw new RoleDeleteIntegrityViolationApiException(role.getRole());
        }
    }

    @Override
    public void lockRole(Integer roleId) {
        Role role = findRoleById(roleId);

        role.setLocked(true);

        roleRepository.save(role);
    }

    @Override
    public void unlockRole(Integer roleId) {
        Role role = findRoleById(roleId);

        role.setLocked(false);

        roleRepository.save(role);
    }

    private Role findRoleById(Integer roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundApiException(roleId));

        return role;
    }

    private String roleNameToConvention(String originalRoleName) {
        String newRoleName = originalRoleName.strip().toUpperCase();

        if (originalRoleName.startsWith(ROLE_PREFIX) == false) {
            newRoleName = ROLE_PREFIX + newRoleName;
        }

        final Pattern pattern = Pattern.compile(ROLE_VALIDATION_REGEX, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(newRoleName);

        if (matcher.find() == false) {
            throw new InvalidRoleNameApiException(newRoleName);
        }

        return newRoleName;
    }

    private Set<Authority> mapAuthoritiesIdToAuthorities(List<Integer> authoritiesId) {
        List<Authority> mappedAuthorities = authorityRepository.findAllById(authoritiesId);

        // Check if valid authority id list was given
        if (mappedAuthorities.size() < authoritiesId.size()) {
            throw new InvalidAuthorityIdListApiException();
        }

        return mappedAuthorities.stream().collect(Collectors.toSet());
    }

}
