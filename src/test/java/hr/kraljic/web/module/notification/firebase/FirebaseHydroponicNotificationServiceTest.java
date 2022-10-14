package hr.kraljic.web.module.notification.firebase;

import hr.kraljic.web.BaseTestEnvironment;
import hr.kraljic.web.module.notification.HydroponicNotification;
import hr.kraljic.web.module.notification.NotificationRepository;
import hr.kraljic.web.module.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;


class FirebaseHydroponicNotificationServiceTest extends BaseTestEnvironment {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    @Test
    @DirtiesContext
    void sendNotification() {
        HydroponicNotification hydroponicNotification = notificationRepository.findById(1).get();
        notificationService.sendNotification(hydroponicNotification);
    }
}