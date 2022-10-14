package hr.kraljic.web.module.hydroponics.sensor;

import hr.kraljic.web.module.hydroponics.sensor.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    /**
     * Pronadji sve senzore koji pripadaju navedenom uredjaju.
     *
     * @param deviceId Id uredaja ciji senzori se zel pronaci
     * @return
     */
    List<Sensor> findAllByDevice_Id(Integer deviceId);
}
