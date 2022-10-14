package hr.kraljic.web.module.hydroponics.sensortype;

import hr.kraljic.web.module.hydroponics.sensortype.model.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorTypeRepository extends JpaRepository<SensorType, Integer> {
}
