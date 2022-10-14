package hr.kraljic.web.module.hydroponics.sensor.variable;

import hr.kraljic.web.config.AbstractAuditAware;
import hr.kraljic.web.module.hydroponics.sensor.model.Sensor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
@Data
@EqualsAndHashCode(exclude = {"sensor"})
public class SimpleVariable extends AbstractAuditAware {
    @Id
    private Integer id;
    private Double minValue;
    private Double maxValue;

    @OneToOne
    @MapsId
    private Sensor sensor;
}
