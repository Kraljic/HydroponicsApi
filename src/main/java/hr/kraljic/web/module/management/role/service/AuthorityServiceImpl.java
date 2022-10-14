package hr.kraljic.web.module.management.role.service;

import hr.kraljic.web.module.core.user.model.dto.AuthorityDto;
import hr.kraljic.web.module.core.user.repository.AuthorityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    private AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public List<AuthorityDto> getAllAuthorities() {
        return authorityRepository.findAll()
                .stream()
                .map(AuthorityDto::map)
                .collect(Collectors.toList());
    }
}
