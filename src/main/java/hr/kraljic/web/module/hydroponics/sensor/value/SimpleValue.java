package hr.kraljic.web.module.hydroponics.sensor.value;

import hr.kraljic.web.module.hydroponics.sensor.model.Sensor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(exclude = {"sensor"})
public class SimpleValue {
    @Id
    private Integer id;
    private Double value;
    private LocalDateTime updatedAt;

    @OneToOne
    @MapsId
    private Sensor sensor;
}
