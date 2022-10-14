package hr.kraljic.web.module.hydroponics.sensortype.model;

import hr.kraljic.web.config.AbstractAuditAware;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class SensorType extends AbstractAuditAware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String type;
    private String measuringUnit;
}
