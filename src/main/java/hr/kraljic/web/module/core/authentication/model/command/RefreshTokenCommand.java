package hr.kraljic.web.module.core.authentication.model.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RefreshTokenCommand {
    @NotEmpty(message = "Access token {valid.NotEmpty}")
    private String accessToken;

    @NotEmpty(message = "Refresh token {valid.NotEmpty}")
    private String refreshToken;
}
