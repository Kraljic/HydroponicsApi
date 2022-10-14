package hr.kraljic.web.module.notification.token.model;

import lombok.Data;

@Data
public class NotificationAccessTokenDto {
    private String token;

    public static NotificationAccessTokenDto map(NotificationAccessToken token) {
        NotificationAccessTokenDto dto = new NotificationAccessTokenDto();
        dto.setToken(token.getToken());

        return dto;
    }
}
