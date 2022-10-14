package hr.kraljic.web.module.hydroponics.sensor.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SensorCommand {
    @NotEmpty(message = "Name {valid.NotEmpty}")
    @Size(min = 3, max = 45, message = "Name {valid.Size.minmax}")
    private String name;

    private Boolean isManaged;
}
