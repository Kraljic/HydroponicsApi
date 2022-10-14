package hr.kraljic.web.module.core.authentication.service;

import hr.kraljic.web.exception.authentication.InvalidRefreshTokenApiException;
import hr.kraljic.web.module.core.authentication.AccessTokenProvider;
import hr.kraljic.web.module.core.authentication.RefreshTokenProvider;
import hr.kraljic.web.module.core.authentication.model.RefreshToken;
import hr.kraljic.web.module.core.authentication.model.command.LoginCommand;
import hr.kraljic.web.module.core.authentication.model.dto.JwtDto;
import hr.kraljic.web.module.core.user.model.ApplicationUser;
import hr.kraljic.web.module.core.user.model.dto.UserDto;
import hr.kraljic.web.module.core.user.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    public AuthServiceImpl(AccessTokenProvider accessTokenProvider,
                           RefreshTokenProvider refreshTokenProvider,
                           AuthenticationManagerBuilder authenticationManagerBuilder,
                           UserService userService) {
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
    }

    @Override
    public JwtDto authenticate(LoginCommand login) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                login.getUsername(),
                login.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        RefreshToken refreshToken = refreshTokenProvider.generateRefreshToken(login.getUsername());

        return JwtDto.map(refreshToken);
    }

    @Override
    public JwtDto refreshToken(String oldRefreshToken, String oldAccessToken) {
        boolean isRefreshTokenValid = refreshTokenProvider.validateRefreshToken(oldRefreshToken, oldAccessToken);
        if (isRefreshTokenValid == false) {
            throw new InvalidRefreshTokenApiException();
        }
        refreshTokenProvider.invalidateRefreshToken(oldRefreshToken, oldAccessToken);

        // Mozemo vjerovati starom JWT tokenu
        String username = accessTokenProvider.getUsernameFromExpiredToken(oldAccessToken);
        RefreshToken refreshToken = refreshTokenProvider.generateRefreshToken(username);

        return JwtDto.map(refreshToken);
    }

    @Override
    public void revokeToken(String oldRefreshToken, String oldAccessToken) {
        boolean isRefreshTokenValid = refreshTokenProvider.validateRefreshToken(oldRefreshToken, oldAccessToken);
        if (isRefreshTokenValid == false) {
            throw new InvalidRefreshTokenApiException();
        }

        refreshTokenProvider.invalidateRefreshToken(oldRefreshToken, oldAccessToken);
    }


    @Override
    public UserDto getCurrentUser() {
        ApplicationUser user = userService.getCurrentUser();

        return UserDto.map(user);
    }
}
