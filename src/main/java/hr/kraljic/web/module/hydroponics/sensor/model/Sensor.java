package hr.kraljic.web.module.hydroponics.sensor.model;

import hr.kraljic.web.config.AbstractAuditAware;
import hr.kraljic.web.module.hydroponics.device.model.Device;
import hr.kraljic.web.module.hydroponics.sensor.value.SimpleValue;
import hr.kraljic.web.module.hydroponics.sensor.variable.SimpleVariable;
import hr.kraljic.web.module.hydroponics.sensortype.model.SensorType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(exclude = {"device", "sensorType"})
public class Sensor extends AbstractAuditAware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Boolean isManaged;

    @OneToOne(mappedBy = "sensor", cascade = CascadeType.REMOVE)
    private SimpleValue simpleValue;
    @OneToOne(mappedBy = "sensor", cascade = CascadeType.ALL)
    private SimpleVariable simpleVariable;

    @ManyToOne
    @JoinColumn(name = "DEVICE_ID", nullable = false)
    private Device device;

    @ManyToOne
    @JoinColumn(name = "SENSOR_TYPE", nullable = false)
    private SensorType sensorType;
}
