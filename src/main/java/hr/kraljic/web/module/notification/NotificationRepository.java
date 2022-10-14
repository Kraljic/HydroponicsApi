package hr.kraljic.web.module.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<HydroponicNotification, Integer> {
}
