package hr.kraljic.web.module.core.authentication.model.dto;

import hr.kraljic.web.module.core.authentication.model.RefreshToken;
import lombok.Data;

@Data
public class JwtDto {
    private String accessToken;
    private String tokenType;
    private String refreshToken;

    public static JwtDto map(RefreshToken refreshToken) {
        JwtDto jwtDto = new JwtDto();
        jwtDto.setAccessToken(refreshToken.getAccessToken());
        jwtDto.setTokenType(refreshToken.getTokenType().getTokenType());
        jwtDto.setRefreshToken(refreshToken.getRefreshToken());

        return jwtDto;
    }
}
