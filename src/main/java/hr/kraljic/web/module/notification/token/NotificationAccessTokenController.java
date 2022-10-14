package hr.kraljic.web.module.notification.token;

import hr.kraljic.web.module.notification.NotificationPermission;
import hr.kraljic.web.module.notification.token.model.NotificationAccessTokenDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notification/api/token")
public class NotificationAccessTokenController {
    private final NotificationAccessTokenService notificationAccessTokenService;

    public NotificationAccessTokenController(NotificationAccessTokenService notificationAccessTokenService) {
        this.notificationAccessTokenService = notificationAccessTokenService;
    }

    @GetMapping
    @NotificationPermission.Access
    public ResponseEntity<NotificationAccessTokenDto> getToken() {
        return ResponseEntity.ok(
                NotificationAccessTokenDto.map(notificationAccessTokenService.getUserToken())
        );
    }

    @GetMapping("reset")
    @NotificationPermission.Access
    public ResponseEntity<NotificationAccessTokenDto> resetToken() {
        return ResponseEntity.ok(
                NotificationAccessTokenDto.map(notificationAccessTokenService.generateNewToken())
        );
    }

}
