package hr.kraljic.web.module.hydroponics.device;

import hr.kraljic.web.module.hydroponics.device.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
}
